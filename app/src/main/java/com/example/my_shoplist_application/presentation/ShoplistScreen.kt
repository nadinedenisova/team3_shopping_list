package com.example.my_shoplist_application.presentation
import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.my_shoplist_application.R
import com.example.my_shoplist_application.domain.models.Ingredients
import com.example.my_shoplist_application.domain.models.MeasurementUnit
import com.example.my_shoplist_application.domain.models.getLabel
import com.example.my_shoplist_application.presentation.model.IngredientListState
import com.example.my_shoplist_application.presentation.model.ShoplistScreenEvent
import com.example.my_shoplist_application.presentation.ui.theme.LocalCustomColor
import com.example.my_shoplist_application.presentation.ui.theme.LocalTypography
import org.koin.androidx.compose.koinViewModel
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ShoplistScreen(listId: Int, onBack: () -> Unit) {
    val viewModel: ShoplistScreenViewModel = koinViewModel()
    val stateIngredient by viewModel.stateIngredient.collectAsState()
    val shopList by viewModel.shoplist.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(listId) {
        viewModel.getShoppingListById(listId)
        viewModel.obtainEvent(ShoplistScreenEvent.Default(listId))
    }
    Scaffold(
        containerColor = LocalCustomColor.current.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(LocalCustomColor.current.background),
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        shopList?.shoplistName.toString(),
                        style = LocalTypography.current.h2
                    )
                },

                navigationIcon = {
                    Row(
                        modifier = Modifier
                            .clickable(onClick = onBack)
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrowback),
                            contentDescription = null,
                            tint = LocalCustomColor.current.blueColor
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            stringResource(R.string.back),
                            style = LocalTypography.current.h3,
                            color = LocalCustomColor.current.blueColor
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.obtainEvent(
                            event = ShoplistScreenEvent.ShowContextMenu
                        )
                    })
                    {
                        Box(
                            modifier = Modifier
                                .padding(end = 16.dp),
                            contentAlignment = Alignment.CenterEnd,
                        ) {
                            Surface(
                                modifier = Modifier
                                    .size(24.dp)
                                    .border(
                                        2.dp,
                                        LocalCustomColor.current.blueColor,
                                        CircleShape
                                    ),
                                shape = CircleShape,
                                color = LocalCustomColor.current.background,
                                content = {},
                            )
                            Text(
                                "•••",
                                color = LocalCustomColor.current.blueColor,
                                modifier = Modifier.padding(end = 3.dp),
                                maxLines = 1
                            )
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                onClick = {
                    if (stateIngredient.showAddPanel) {
                        viewModel.obtainEvent(ShoplistScreenEvent.OnAddingIngredientBtnClick)
                    } else if (stateIngredient.isSelectProducts) {
                        viewModel.obtainEvent(ShoplistScreenEvent.OnDeleteBtnInContextMenuClick)

                    }
                },
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                containerColor = if (stateIngredient.showAddPanel || stateIngredient.isSelectProducts) LocalCustomColor.current.buttonColorBlueWhite else LocalCustomColor.current.grey
            ) {
                Text(
                    text = if (stateIngredient.showAddPanel) "Готово" else "Все в корзине",
                    color = if (stateIngredient.showAddPanel || stateIngredient.isSelectProducts) LocalCustomColor.current.textColorWhiteBlue else LocalCustomColor.current.textColorWhiteGrey,
                    style = LocalTypography.current.h2
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->

        Box(modifier = Modifier.padding(padding)) {
            ShowIngridientList(
                context = context,
                viewModel,
                state = stateIngredient,
                onDelete = {
                    viewModel.obtainEvent(
                        ShoplistScreenEvent.OnDeleteIngredientSwipeClick(
                            it
                        )
                    )
                },
                onUpdateBough = {
                    viewModel.obtainEvent(
                        ShoplistScreenEvent.OnUpdateBoughtIngredientClick(
                            it
                        )
                    )
                },
                onClick = { viewModel.obtainEvent(ShoplistScreenEvent.ShowAddPanel) },
                onItemNameChange = { viewModel.obtainEvent(ShoplistScreenEvent.UpdateItemName(it)) },
                onSuggestionSelected = { suggestion ->
                    viewModel.obtainEvent(
                        ShoplistScreenEvent.UpdateItemName(
                            suggestion
                        )
                    )
                },
                onDismiss = { viewModel.obtainEvent(ShoplistScreenEvent.HideAddPanel) },
            )

            if (stateIngredient.showContextMenu) {
                ContextMenu(
                  //  position = stateIngredient.contextMenuPosition,
                    onSorting = { viewModel.obtainEvent(ShoplistScreenEvent.OnSortBtnInContextMenuClick) },
                    onDismiss = { viewModel.obtainEvent(ShoplistScreenEvent.HideContextMenu) },
                    onClear = { viewModel.obtainEvent(ShoplistScreenEvent.OnDeleteBtnInContextMenuClick) }
                )
            }

        }
    }
}

// Показать список
@Composable
fun ShowIngridientList(
    context: Context,
    viewModel: ShoplistScreenViewModel,
    state: IngredientListState,
    onDelete: (Ingredients) -> Unit,
    onUpdateBough: (Ingredients) -> Unit,
    onClick: () -> Unit,
    onItemNameChange: (String) -> Unit,
    onSuggestionSelected: (String) -> Unit,
    onDismiss: () -> Unit,

    ) {

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SwitchWithText(state.isSelectProducts) { isChecked ->
                viewModel.obtainEvent(
                    ShoplistScreenEvent.OnUpdateAllBoughtIngredientClick(isChecked)
                )
            }
            Box(modifier = Modifier.fillMaxWidth()) {

                PanelAddUp(
                    state = state,
                    onItemNameChange = onItemNameChange,
                    onSuggestionSelected = onSuggestionSelected,
                    onDismiss = onDismiss
                )
            }

            LazyColumn {
                items(state.ingredients.reversed(), key = { it.id }) { item ->
                    SwipeableItem(
                        context = context,
                        item = item,
                        onDelete = { onDelete(item) },
                        onClick = { },
                        onUpdateBough = { onUpdateBough(item) }
                    )
                    HorizontalDivider(Modifier.padding(start = 64.dp))
                }
            }

            if (!state.showAddPanel) {
                TextButton(
                    modifier = Modifier.align(Alignment.Start),
                    onClick = onClick,
                ) {
                    Text(
                        stringResource(R.string.add_ingredient),
                        Modifier.padding(start = 16.dp, top = 20.dp),
                        style = LocalTypography.current.h3,
                        color = LocalCustomColor.current.blueColor
                    )

                }
            }
        }

        if (state.showAddPanel) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(LocalCustomColor.current.background)
                    .padding(bottom = 98.dp, top = 8.dp, start = 16.dp, end = 16.dp)
            ) {
                PanelAddDown(viewModel = viewModel, state = state, context = context)

            }
        }

    }
}

// Поле для ввода названия ингредиента
@Composable
fun PanelAddUp(
    state: IngredientListState,
    onItemNameChange: (String) -> Unit,
    onSuggestionSelected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    if (state.showAddPanel) {
        var expanded by remember { mutableStateOf(false) }
        var textFieldWidth by remember { mutableStateOf(0) }
        val focusRequester = remember { FocusRequester() }
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    modifier = Modifier.scale(1.3f),
                    onCheckedChange = { },
                    checked = false,
                )

                TextField(
                    value = state.newItemName,
                    onValueChange = {
                        onItemNameChange(it)
                        expanded = it.isNotEmpty() && state.suggestions.isNotEmpty()
                    },
                    label = {
                        Text(
                            stringResource(R.string.name_ingredient),
                            color = LocalCustomColor.current.textColorCrossed
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onGloballyPositioned { coordinate ->
                            textFieldWidth = coordinate.size.width
                        },
                    textStyle = LocalTypography.current.h3,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            Modifier.clickable { onDismiss() },
                            tint = LocalCustomColor.current.grey
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Red,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent
                    )
                )

                DropdownMenu(
                    shadowElevation = 0.dp,
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    properties = PopupProperties(focusable = false),
                    modifier = Modifier.width(with(LocalDensity.current) { textFieldWidth.toDp() }),
                    containerColor = LocalCustomColor.current.background
                ) {
                    state.suggestions.take(5).forEach { suggestion ->
                        DropdownMenuItem(
                            onClick = {
                                onSuggestionSelected(suggestion)
                                expanded = false
                            },
                            text = {
                                Text(text = suggestion)
                            }
                        )
                    }
                }

                LaunchedEffect(state.showAddPanel) {
                    if (state.showAddPanel) {
                        focusRequester.requestFocus()
                    }
                }
            }
            HorizontalDivider(Modifier.padding(start = 64.dp))

        }
    }
}

// Нижняя панель добавления ингредиентов
@Composable
fun PanelAddDown(
    viewModel: ShoplistScreenViewModel,
    state: IngredientListState,
    context: Context
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .background(Color(0xFF9DD8EC), RoundedCornerShape(9.dp)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MeasurementUnit.entries.forEachIndexed { index, unit ->
            val isSelected = unit == state.newItemUnit
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(9.dp))
                    .background(
                        if (isSelected) Color.White else Color.Transparent
                    )
                    .border(
                        width = if (isSelected) 2.dp else 0.dp,
                        color = if (isSelected) Color(0xFF9DD8EC) else Color.Transparent,
                        shape = RoundedCornerShape(9.dp)
                    )
                    .clickable {
                        viewModel.obtainEvent(ShoplistScreenEvent.ChangeAddUnit(unit))
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = unit.getLabel(context),
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color.Black else Color.White,
                    style = LocalTypography.current.h4
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
    // Счетчик
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
    ) {
        IconButton(
            modifier = Modifier
                .height(50.dp)
                .width(50.dp),
            onClick = {
                if (state.newItemQuantity > 1) viewModel.obtainEvent(
                    ShoplistScreenEvent.ChangeAddQuantity(state.newItemQuantity - 1)
                )
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.button_mines),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        Text(
            "${state.newItemQuantity}",
            modifier = Modifier.width(32.dp),
            textAlign = TextAlign.Center,
            style = LocalTypography.current.h3,
            color = LocalCustomColor.current.textColor
        )
        IconButton(
            modifier = Modifier
                .height(50.dp)
                .width(50.dp),
            onClick = {
                viewModel.obtainEvent(
                    ShoplistScreenEvent.ChangeAddQuantity(
                        state.newItemQuantity + 1
                    )
                )
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.button_plus),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeableItem(
    context: Context,
    item: Ingredients,
    onDelete: () -> Unit,
    onClick: () -> Unit,
    onUpdateBough: () -> Unit
) {

    var isRevealed by remember { mutableStateOf(false) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    val resetSwipeState = {
        isRevealed = false
        offsetX = 0f
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Фон с кнопками при свайпе
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    onDelete()
                    resetSwipeState()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = LocalCustomColor.current.red
                ),
                modifier = Modifier.size(width = 143.dp, height = 44.dp),
                shape = RoundedCornerShape(0.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(stringResource(R.string.delete), style = LocalTypography.current.h3)
            }
        }
        Surface(
            color = LocalCustomColor.current.background,
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = offsetX.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (offsetX < -150f) {
                                // Показываем кнопки
                                offsetX = -300f
                                isRevealed = true
                            } else {
                                // Скрываем кнопки
                                offsetX = 0f
                                isRevealed = false
                            }
                        },
                        onDragCancel = {
                            offsetX = if (isRevealed) -300f else 0f
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            val newValue = (offsetX + dragAmount)
                            offsetX = newValue.coerceIn(-300f, 0f)
                        }
                    )
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, end = 16.dp)
                    .height(44.dp)
                    .combinedClickable(
                        onClick = { onClick /*действие при обычном клике*/ },
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    modifier = Modifier.scale(1.3f),
                    checked = item.isBought,
                    onCheckedChange = { onUpdateBough() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = LocalCustomColor.current.background,
                        checkmarkColor = LocalCustomColor.current.checkBoxColor,
                    )
                )
                Text(
                    text = item.ingredientName,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .clickable { },
                    style = LocalTypography.current.h3,
                    color = if (item.isBought) LocalCustomColor.current.textColorCrossed else LocalCustomColor.current.textColor,
                    textDecoration = if (item.isBought) TextDecoration.LineThrough else TextDecoration.None
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    modifier = Modifier.padding(end = 12.dp),
                    text = " ${item.ingredientQuantity} ${item.ingredientUnit.getLabel(context)}",
                    style = LocalTypography.current.h3,
                    color = LocalCustomColor.current.textColorLabe,
                )
                // Стрелка
                Icon(
                    painter = painterResource(R.drawable.chevron),
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    }
}
@Composable
fun SwitchWithText(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.Select_products),
            style = LocalTypography.current.h3,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .weight(1f),
            color = LocalCustomColor.current.textColor
        )
        Switch(
            modifier = Modifier.padding(horizontal = 16.dp),
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = LocalCustomColor.current.colorTrackTint,
                checkedTrackColor = LocalCustomColor.current.colorTrackTintCheck,
                uncheckedThumbColor = LocalCustomColor.current.colorTrackTint,
                uncheckedTrackColor = LocalCustomColor.current.ucheckedTrackColor
            )
        )
    }
}

@Composable
fun ContextMenu(
    onSorting: () -> Unit,
    onDismiss: () -> Unit,
    onClear: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Popup(
            alignment = Alignment.TopEnd,
            offset = IntOffset(0, 0),
            onDismissRequest = onDismiss
        ) {
            Card(
                modifier = Modifier.width(250.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(LocalCustomColor.current.background)
            ) {
                Column {
                    DropdownMenuItem(
                        text = {
                            Text(
                                stringResource(R.string.sorting),
                                style = LocalTypography.current.h3
                            )
                        },
                        onClick = {
                            onSorting()
                            onDismiss()
                        },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.symbol),
                                contentDescription = null
                            )
                        }
                    )
                    HorizontalDivider()

                    DropdownMenuItem(
                        text = {
                            Text(
                                stringResource(R.string.clear_list),
                                style = LocalTypography.current.h3
                            )
                        },
                        onClick = {
                            onClear()
                            onDismiss()
                        },
                        trailingIcon = { Icon(Icons.Default.Clear, null) }

                    )

                }
            }
        }
    }
}





