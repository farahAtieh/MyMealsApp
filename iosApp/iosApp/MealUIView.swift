//
//  MealUIView.swift
//  iosApp
//
//  Created by Farah Atieh on 22/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared
import Kingfisher

struct MealUIView: View {
    var meal: Meal
    var onFavouriteTapped: (Meal) -> Void = {_ in }

    var body: some View{
        VStack{
            KFImage(URL(string: meal.imageUrl))
                .resizable()
                .aspectRatio(1, contentMode: .fit)
                .cornerRadius(16)
            HStack{
                Text(meal.name)
                    .padding(4)
                    .lineLimit(1)
                    .truncationMode(.tail)
                Spacer()
                Button(action: {
                    onFavouriteTapped(meal)
                }, label: {
                    if(meal.isFavourite){
                        Image(systemName: "heart.fill")
                            .resizable()
                            .aspectRatio(1, contentMode: .fit)
                            .frame(width: 24)

                    } else {
                        Image(systemName: "heart")
                            .resizable()
                            .aspectRatio(1, contentMode: .fit)
                            .frame(width: 24)

                    }
                }).padding(4)
            }
        }

    }
}

