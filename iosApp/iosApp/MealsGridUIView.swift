//
//  MealsGridUIView.swift
//  iosApp
//
//  Created by Farah Atieh on 22/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

struct MealsGridUIView: View {
    var meals: Array<Meal>
    var onFavouriteTapped: (Meal) -> Void = {_ in}

    var body: some View{
        let columns = [
            GridItem(.flexible(minimum: 128, maximum: 256), spacing: 16),
            GridItem(.flexible(minimum: 128, maximum: 256), spacing: 16)
        ]
        ScrollView{
            LazyVGrid(columns: columns,spacing: 16){
                ForEach(meals, id: \.name){ meal in
                    MealUIView(meal: meal,
                    onFavouriteTapped: onFavouriteTapped)
                }
            }.padding(.horizontal, 16)
        }
    }
}

