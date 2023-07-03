import SwiftUI
import shared

struct ContentView: View {

    @ObservedObject
    private var mealsState = MealsState()
    
    init() {
        KoinModuleKt.doInitKoin()
    }
    
    var body: some View {
        VStack{
            Toggle("Filter favourite", isOn: $mealsState.shouldFilterFavourites)
                .padding(16)
            
            ZStack{
                switch mealsState.state{
                case ViewState.loading:
                    ProgressView().frame(alignment: .center)
                case ViewState.normal:
                    MealsGridUIView(
                        meals: mealsState.filteredMeals,
                        onFavouriteTapped: mealsState.onFavouriteSelected)
                    
                case ViewState.empty:
                    Text("Ooops looks like there are no meals")
                        .frame(alignment: .center)
                        .font(.headline)
                    
                case ViewState.error:
                    Text("Ooops something went wrong...")
                        .frame(alignment: .center)
                        .font(.headline)
                }
            }
            Spacer()
        }.frame(maxWidth: .infinity, maxHeight: .infinity)
        
    }
}

