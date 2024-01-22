package com.sharath070.jettip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharath070.jettip.components.InputField
import com.sharath070.jettip.ui.theme.JetTipTheme
import com.sharath070.jettip.utils.calculateTotalPerPerson
import com.sharath070.jettip.utils.calculateTotalTip
import com.sharath070.jettip.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                Column {
                    MainContent()
                }
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    JetTipTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = Color.White) {
            content()
        }
    }
}


@Composable
fun TopHeader(totalPerPerson: Double = 0.0) {
    Spacer(modifier = Modifier.height(13.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(17.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(Color(0xFFDAC1EE))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val total = "%.2f".format(totalPerPerson)

            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Text(
                text = "$$total",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
        }
    }
    Spacer(modifier = Modifier.height(3.dp))
}

//@Preview(showBackground = true)
@Composable
fun MainContent() {
    val splitBillState = remember {
        mutableIntStateOf(1)
    }
    val tipAmountState = remember {
        mutableDoubleStateOf(0.0)
    }
    val totalPerPersonState = remember {
        mutableDoubleStateOf(0.0)
    }
    BillForm(
        splitBillState = splitBillState,
        tipAmountState = tipAmountState,
        totalPerPersonState = totalPerPersonState
    ){}
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    splitBillState: MutableState<Int>,
    tipAmountState: MutableState<Double>,
    totalPerPersonState: MutableState<Double>,
    onValChanged: (String) -> Unit
) {

    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    val sliderPositionState = remember {
        mutableFloatStateOf(0f)
    }
    val tipPercentage = (sliderPositionState.floatValue * 100).toInt()


    TopHeader(totalPerPerson = totalPerPersonState.value)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(7.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputField(
                valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions
                    onValChanged(totalBillState.value.trim())
                    keyboardController?.hide()
                },
                modifier = Modifier.fillMaxWidth()
            )

            if (validState) {
                Row(
                    modifier = modifier
                        .padding(3.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Split",
                        modifier = modifier
                            .weight(1f)
                            .padding(start = 7.dp),
                        fontSize = 17.sp
                    )

                    Row(
                        modifier = modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        RoundIconButton(imageVector = Icons.Default.Remove) {
                            if (splitBillState.value > 1)
                                splitBillState.value -= 1
                            else
                                splitBillState.value = 1

                            totalPerPersonState.value =
                                calculateTotalPerPerson(
                                    totalBillState.value.toDouble(),
                                    splitBillState.value,
                                    tipPercentage
                                )
                        }
                        Text(
                            text = "${splitBillState.value}",
                            modifier = modifier.padding(horizontal = 9.dp),
                            fontSize = 17.sp
                        )
                        RoundIconButton(imageVector = Icons.Default.Add) {
                            splitBillState.value += 1

                            totalPerPersonState.value =
                                calculateTotalPerPerson(
                                    totalBillState.value.toDouble(),
                                    splitBillState.value,
                                    tipPercentage
                                )
                        }
                    }
                }
                Row(modifier = modifier.padding(vertical = 12.dp)) {
                    Text(
                        text = "Tip",
                        modifier = modifier
                            .weight(1f)
                            .padding(start = 10.dp),
                        fontSize = 17.sp
                    )
                    Text(
                        text = "$${tipAmountState.value}",
                        modifier = modifier
                            .weight(1f)
                            .wrapContentSize(Alignment.Center),
                        fontSize = 17.sp
                    )
                }

                Text(
                    text = "$tipPercentage%",
                    modifier = Modifier.padding(10.dp),
                    fontSize = 25.sp
                )

                Slider(
                    value = sliderPositionState.floatValue,
                    onValueChange = { newValue ->
                        sliderPositionState.floatValue = newValue
                        tipAmountState.value =
                            calculateTotalTip(totalBillState.value.toDouble(), tipPercentage)

                        totalPerPersonState.value =
                            calculateTotalPerPerson(
                                totalBillState.value.toDouble(),
                                splitBillState.value,
                                tipPercentage
                            )
                    },
                    modifier = Modifier.padding(horizontal = 15.dp)
                )

            } else {
                Box(Modifier)
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApp {
        Column {
            MainContent()
        }
    }
}