import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.ceil
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.Role

// ViewModel class to manage the state
class PizzaPartyViewModel : ViewModel() {

    // State properties
    var numPeopleInput by mutableStateOf("")
        private set

    var hungerLevel by mutableStateOf("Medium")
        private set

    var totalPizzas by mutableStateOf(0)
        private set

    // Update the number of people input
    fun updateNumPeopleInput(input: String) {
        numPeopleInput = input
    }

    // Update the hunger level
    fun updateHungerLevel(level: String) {
        hungerLevel = level
    }

    // Calculate the total number of pizzas
    fun calculatePizzas() {
        val numPeople = numPeopleInput.toIntOrNull() ?: 0
        totalPizzas = calculateNumPizzas(numPeople, hungerLevel)
    }
}

@Composable
fun PizzaPartyScreen(viewModel: PizzaPartyViewModel = viewModel(), modifier: Modifier = Modifier) {

    Column(
        modifier = modifier.padding(10.dp)
    ) {
        // Title
        Text(
            text = "Pizza Party",
            fontSize = 38.sp,
            modifier = modifier.padding(bottom = 16.dp)
        )

        // Input field for number of people
        NumberField(
            labelText = "Number of people?",
            textInput = viewModel.numPeopleInput,
            onValueChange = { viewModel.updateNumPeopleInput(it) },
            modifier = modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
        )

        // Radio buttons for hunger level
        RadioGroup(
            labelText = "How hungry?",
            radioOptions = listOf("Light", "Medium", "Hungry", "Very hungry"),
            selectedOption = viewModel.hungerLevel,
            onSelected = { viewModel.updateHungerLevel(it) },
            modifier = modifier
        )

        // Text showing the total number of pizzas
        Text(
            text = "Total pizzas: ${viewModel.totalPizzas}",
            fontSize = 22.sp,
            modifier = modifier.padding(top = 16.dp, bottom = 16.dp)
        )

        // Calculate button
        Button(
            onClick = {
                viewModel.calculatePizzas()
            },
            modifier = modifier.fillMaxWidth()
        ) {
            Text("Calculate")
        }
    }
}

// NumberField Composable
@Composable
fun NumberField(
    labelText: String,
    textInput: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = textInput,
        onValueChange = onValueChange,
        label = { Text(labelText) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
    )
}

// RadioGroup Composable
@Composable
fun RadioGroup(
    labelText: String,
    radioOptions: List<String>,
    selectedOption: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isSelectedOption: (String) -> Boolean = { selectedOption == it }

    Column {
        Text(labelText)
        radioOptions.forEach { option ->
            Row(
                modifier = modifier
                    .selectable(
                        selected = isSelectedOption(option),
                        onClick = { onSelected(option) },
                        role = Role.RadioButton
                    )
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = isSelectedOption(option),
                    onClick = null, // The click is handled by Row
                    modifier = modifier.padding(end = 8.dp)
                )
                Text(
                    text = option,
                    modifier = modifier.fillMaxWidth()
                )
            }
        }
    }
}

// Pizza calculation logic
fun calculateNumPizzas(
    numPeople: Int,
    hungerLevel: String
): Int {
    val slicesPerPizza = 8
    val slicesPerPerson = when (hungerLevel) {
        "Light" -> 2
        "Medium" -> 3
        "Hungry", "Very hungry" -> 5
        else -> 3
    }

    // Calculate the number of pizzas and round up to the nearest whole pizza
    return ceil(numPeople * slicesPerPerson / slicesPerPizza.toDouble()).toInt()
}
