//
//  MainViewModel.swift
//  iosApp
//
//  Created by Farah Atieh on 22/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import KMPNativeCoroutinesRxSwift
import shared
import RxSwift

class MainViewModel: ObservableObject {
    private let repository = MealsRepository.init()
    private let getMeals = GetMealsUseCase.init()
    private let fetchMeals = FetchMealsUseCase.init()
    private let onToggleFavouriteState = ToggleFavouriteStateUseCase.init()
    @Published
    private(set) var state = State.LOADING
    @Published
    var shouldFilterFavourites = false
    @Published
    private(set) var filteredMeals: [Meal] = []
    @Published
    private var meals: [Meal] = []
    private let disposeBag = DisposeBag()

    init(){
        createObservable(for: repository.mealsFlow)
            .subscribe(onNext: { meals in
                DispatchQueue.main.async {
                    self.meals = meals
                }
            }).disposed(by: disposeBag)
        
        
        $meals.combineLatest($shouldFilterFavourites, {
            meals, shouldFilterFavourites -> [Meal] in
            
            var result: [Meal] = []
            
            if(shouldFilterFavourites){
                result.append(contentsOf:
                                meals.filter{$0.isFavourite})
            }else {
                result.append(contentsOf: meals)
            }
            
            if(result.isEmpty){
                self.state = State.EMPTY
            }else {
                self.state = State.NORMAL
            }
            
            return result
        }).assign(to: &$filteredMeals)
    }
    
    func getData(){
        state = State.LOADING
        createSingle(for: getMeals.invoke(parameterValue: getRandomChar()))
            .subscribe(
            onSuccess: { _ in
                DispatchQueue.main.async {
                    self.state = State.NORMAL
                }
            }, onFailure: { error in
                DispatchQueue.main.async {
                    self.state = State.ERROR
                }
            }).disposed(by: disposeBag)
    }
    
    func fetchData(){
        state = State.LOADING
        createSingle(for: fetchMeals.invoke(parameterValue: getRandomChar()))
            .subscribe(onSuccess: { _ in
                DispatchQueue.main.async {
                    self.state = State.NORMAL
                }
            }, onFailure: {error in
                DispatchQueue.main.async {
                    self.state = State.ERROR
                }
            }).disposed(by: disposeBag)
    }
    
    func onFavouriteTapped(meal : Meal){
        createSingle(for: onToggleFavouriteState.invoke(meal: meal))
            .subscribe()
            .disposed(by: disposeBag)
    }
    
    // Use this method to generate a random number between 65 and 90 (ASCII values for A to Z)
    private func getRandomChar() -> String {
        let asciiValue = Int.random(in: 65...90)
        guard let char = UnicodeScalar(asciiValue) else {
            return ""
        }
        return String(char)
    }
}

