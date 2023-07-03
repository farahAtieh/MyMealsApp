//
//  MealsState.swift
//  iosApp
//
//  Created by Farah Atieh on 29/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class MealsState: ObservableObject{
    
    let mainViewModel = MainViewModel()
    
    @Published
    private(set) var state: ViewState = .loading
    @Published
    var shouldFilterFavourites = false
    @Published
    private(set) var filteredMeals: [Meal] = []
    @Published
    private var meals: [Meal] = []
    
    init() {
        mainViewModel.observeMeals{ items in
            self.meals = items
        }
        
        mainViewModel.observeState { state in
            self.state = state.toViewState()
            
        }
    }
    
    func onFavouriteSelected(meal: Meal){
        mainViewModel.onFavouriteTapped(meal: meal)
    }
    
    deinit{
        mainViewModel.dispose()
    }
}

private extension MainViewModel.State {
    func toViewState() -> ViewState {
        switch self {
        case .loading: return .loading
        case .normal: return .normal
        case .empty: return .empty
        case .error: return .error
        default: return .loading
        }
    }
}
