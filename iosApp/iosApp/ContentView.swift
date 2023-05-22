import SwiftUI
import shared

struct ContentView: View {

    @ObservedObject
    private var viewModel: MainViewModel
    
    init() {
        KoinModuleKt.doInitKoin()
        viewModel = MainViewModel.init()
    }
    
    var body: some View {
        VStack{
            Toggle("Filter favourite", isOn: $viewModel.shouldFilterFavourites)
                .padding(16)
            Button("Refresh Meals", action: {
                viewModel.fetchData()
            })
            .frame(alignment: .center)
            .padding(.bottom, 16)
            ZStack{
                switch viewModel.state{
                case State.LOADING:
                    ProgressView().frame(alignment: .center)
                case State.NORMAL:
                    MealsGridUIView(
                        meals: viewModel.filteredMeals,
                        onFavouriteTapped: viewModel.onFavouriteTapped)
                    
                case State.EMPTY:
                    Text("Ooops looks like there are no meals")
                        .frame(alignment: .center)
                        .font(.headline)
                    
                case State.ERROR:
                    Text("Ooops something went wrong...")
                        .frame(alignment: .center)
                        .font(.headline)
                }
            }
            Spacer()
        }.frame(maxWidth: .infinity, maxHeight: .infinity)
        
    }
}

