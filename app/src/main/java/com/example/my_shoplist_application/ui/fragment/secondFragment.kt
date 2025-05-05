package com.example.my_shoplist_application.ui.fragment

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.my_shoplist_application.R
import com.example.my_shoplist_application.domain.models.Ingredients
import com.example.my_shoplist_application.domain.models.MeasurementUnit
import com.example.my_shoplist_application.domain.models.Shoplist
import com.example.my_shoplist_application.presentation.ShoplistScreenViewModel
import com.example.my_shoplist_application.presentation.model.IngredientListState
import com.example.my_shoplist_application.presentation.model.IngredientListState.SortOrder
import com.example.my_shoplist_application.presentation.model.ShoplistScreenEvent
import com.example.my_shoplist_application.ui.theme.LocalCustomColor
import com.example.my_shoplist_application.ui.theme.LocalTypography
import com.example.my_shoplist_application.ui.viewmodel.ShoppingList
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun IngredientsListScreen(listId: Int, onBack: () -> Unit) {
    val viewModel: ShoplistScreenViewModel = koinViewModel()
    val stateIngredient by viewModel.stateIngredient.collectAsState()
    val shoplist by viewModel.shoplist.collectAsState()
    val isDialogVisible by viewModel.isDialogVisible.collectAsState()
    val isDialogDeleteVisible by viewModel.isDialogDeleteVisible.collectAsState()

    LaunchedEffect(listId) {
        viewModel.getShoppingListById(listId)
        viewModel.obtainEvent(ShoplistScreenEvent.Default(listId))
    }

    Scaffold(
        containerColor = LocalCustomColor.current.background,
        topBar = {
            TopAppBar(
                title = { Text(shoplist?.shoplistName.toString()) },
                actions = {
                    IconButton(onClick = {
                        viewModel.obtainEvent(
                            event = ShoplistScreenEvent.OnContextMenuIconClick(Offset(0f, 0f))
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
                navigationIcon = {
                    Row(
                        modifier = Modifier
                            .clickable(onClick = onBack)
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrowback),
                            contentDescription = stringResource(R.string.back),
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
            )
        },
        floatingActionButton = {// кнопка добавить снизу
            FloatingActionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                onClick = { viewModel.showDialog()/*Открыть экран*/ },
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                containerColor = LocalCustomColor.current.grey
            ) {
                Text(
                    "Все в корзине",
                    color = LocalCustomColor.current.textColorWhiteGrey,
                    style = LocalTypography.current.h2
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center

    ) { padding ->

        Box(modifier = Modifier.padding(padding)) {
            ShowIngridientList(
                viewModel = viewModel,
                state = stateIngredient,
                onDelete = {
                    viewModel.obtainEvent(
                        ShoplistScreenEvent.OnDeleteIngredientSwipeClick(
                            it
                        )
                    )
                },
                onTogglePurchased = {
                    shoplist?.let { shopList ->
                        viewModel.obtainEvent(
                            ShoplistScreenEvent.OnIsBoughtIngredientClick(it, shopList)
                        )
                    }
                }
            )

            if (isDialogVisible) {
                DialogAddProduct(
                    newItemName = stateIngredient.newItemName,
                    onItemNameChange = { viewModel.obtainEvent(ShoplistScreenEvent.UpdateItemName(it)) },
                    suggestions = stateIngredient.suggestions,
                    onSuggestionSelected = { suggestion ->
                        viewModel.obtainEvent(ShoplistScreenEvent.UpdateItemName(suggestion))
                    },
                    viewModel,
                    shoplist
                )
            }

            if (stateIngredient.showContextMenu) {
                ContextMenu(
                    position = stateIngredient.contextMenuPosition,
                    onSorting = {},
                    onDismiss = { },
                    onClear = { }
                )
            }

        }
    }
}

// Диалог добавления продукта
@Composable
fun DialogAddProduct(
    newItemName: String,
    onItemNameChange: (String) -> Unit,
    suggestions: List<String>,
    onSuggestionSelected: (String) -> Unit,
    viewModel: ShoplistScreenViewModel,
    shoplist: Shoplist?
) {
    var newItemQuantity by remember { mutableStateOf("") }
    var newItemUnit by remember { mutableStateOf(MeasurementUnit.PCS) }

    AlertDialog(
        onDismissRequest = { viewModel.hideDialog() },
        title = { Text("Создать новый список", style = LocalTypography.current.h3) },
        text = {
            Column {
                Column(modifier = Modifier.fillMaxWidth()) {
                    var expanded by remember { mutableStateOf(false) }
                    var textFieldWidth by remember { mutableStateOf(0) }
                    OutlinedTextField(
                        value = newItemName,
                        onValueChange = {
                            onItemNameChange(it)
                            expanded = it.isNotEmpty() && suggestions.isNotEmpty()
                        },
                        label = { Text("Название списка", style = LocalTypography.current.h3) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinate ->
                                textFieldWidth = coordinate.size.width
                            },
                        textStyle = LocalTypography.current.h3,
                        trailingIcon = {
                            Icon(
                                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                Modifier.clickable { expanded = !expanded }
                            )
                        }
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        properties = PopupProperties(focusable = false),
                        modifier = Modifier.width(with(LocalDensity.current) { textFieldWidth.toDp() })
                    ) {
                        suggestions.take(5).forEach { suggestion ->
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
                }

                Row {
                    OutlinedTextField(
                        value = newItemQuantity,
                        onValueChange = { newItemQuantity = it },
                        label = { Text("Количество") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Box(Modifier.weight(1F)) {
                        var expanded by remember { mutableStateOf(false) }
                        OutlinedTextField(
                            value = newItemUnit.label,
                            onValueChange = { },
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            trailingIcon = {
                                IconButton(onClick = { expanded = !expanded }) {
                                    Icon(Icons.Default.ArrowDropDown, null)
                                }
                            }
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            MeasurementUnit.values().forEach { unit ->
                                DropdownMenuItem(
                                    text = { Text(unit.label) },
                                    onClick = {
                                        newItemUnit = unit
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },

        // Поле для ввода количества
        confirmButton = {
            Button(
                onClick = {
                    val quantityFloat = newItemQuantity.toFloatOrNull()
                    if (newItemName.isNotBlank() && quantityFloat != null) {
                        viewModel.obtainEvent(
                            ShoplistScreenEvent.OnAddingIngredientBtnClick(
                                newItemName,
                                newItemQuantity.toFloat(),
                                newItemUnit,
                                listId = shoplist?.id
                            )
                        )
                        onItemNameChange("")
                        newItemQuantity = ""
                        viewModel.hideDialog()
                    }
                }
            ) {
                Text("Создать", style = LocalTypography.current.h3)
            }
        },
        dismissButton = {
            TextButton(onClick = { viewModel.hideDialog() }) {
                Text("Отмена", style = LocalTypography.current.h3)
            }
        }
    )
}


// Показать список
@Composable
fun ShowIngridientList(
    viewModel: ShoplistScreenViewModel,
    state: IngredientListState,
    onDelete: (Ingredients) -> Unit,
    onTogglePurchased: (Ingredients) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedList by remember { mutableStateOf<ShoppingList?>(null) }

  //   Функция для открытия диалога
    val openDialog: (ShoppingList) -> Unit = { list ->
        selectedList = list
        showDialog = true
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SwitchWithText(false) { /*isChecked -> viewModel.switch(isChecked) */}

        LazyColumn {
            items(state.ingredients, key = { it.id }) { item ->
                SwipeableItem(
                    item = item,
                    onDelete = { onDelete(item) },
                    onClick = {},
                    onTogglePurchased = { onTogglePurchased(item) }
                )
                HorizontalDivider(Modifier.padding(start = 64.dp))
            }
        }

        TextButton(
            modifier = Modifier.align(Alignment.Start),
            onClick = { viewModel.showDialog() /*Открыть диалог Добавить продукт*/ },
        ) {
            Text(
                "Добавить продукт",
                Modifier.padding(start = 16.dp, top = 20.dp),
                style = LocalTypography.current.h3,
                color = LocalCustomColor.current.blueColor
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeableItem(
    item: Ingredients,
    onDelete: () -> Unit,
    onClick: () -> Unit,
    onTogglePurchased: () -> Unit
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
                    containerColor = Color(0xFFF44336)
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
                    checked = item.isBought,
                    onCheckedChange = { onTogglePurchased() }
                )
                Text(
                    text = "${item.ingredientName} ", Modifier.padding(start = 16.dp),
                    style = LocalTypography.current.h3,
                    color = LocalCustomColor.current.textColor,
                    textDecoration = if (item.isBought) TextDecoration.LineThrough else TextDecoration.None
                )
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    modifier = Modifier.padding(end = 12.dp),
                    text = " ${item.ingredientQuantity} ${item.ingredientUnit.label}",
                    style = LocalTypography.current.h3,
                    color = LocalCustomColor.current.textColorLabe,
                    textDecoration = if (item.isBought) TextDecoration.LineThrough else TextDecoration.None
                )
                // Стрелка
                Icon(
                    painter = painterResource(R.drawable.chevron),
                    contentDescription = "Открыть",
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
            text = "Выделить все продукты",
            style = LocalTypography.current.h3,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .weight(1f)
        )
        Switch(
            modifier = Modifier.padding(horizontal = 16.dp),
            checked = isChecked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
fun ContextMenu(
    position: Offset,
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
                modifier = Modifier.width(200.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column {
                    DropdownMenuItem(
                        text = { Text("Сортировка") },
                        onClick = {
                            onSorting()
                            onDismiss
                        },
                        leadingIcon = { Icon(Icons.Default.Star, null) }
                    )

                    DropdownMenuItem(
                        text = { Text("Очистить список") },
                        onClick = {
                            onClear()
                            onDismiss()
                        },
                        leadingIcon = { Icon(Icons.Default.Clear, null) }

                    )

                }
            }
        }
    }
}

// сортировка
@Composable
fun SortingSection(
    currentSortOrder: SortOrder,
    onSortOrderChange: (SortOrder) -> Unit
) {
    FilterChip(
        selected = currentSortOrder == SortOrder.ALPHABETICAL,
        onClick = { onSortOrderChange(SortOrder.ALPHABETICAL) },
        label = { Text("По алфавиту") }
    )
}


