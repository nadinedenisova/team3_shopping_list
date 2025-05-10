package com.example.my_shoplist_application.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.my_shoplist_application.R
import com.example.my_shoplist_application.domain.models.Shoplist
import com.example.my_shoplist_application.presentation.model.MainScreenEvent
import com.example.my_shoplist_application.presentation.model.ShoppingListEvent
import com.example.my_shoplist_application.presentation.ui.theme.LocalCustomColor
import com.example.my_shoplist_application.presentation.ui.theme.LocalTypography
import org.koin.androidx.compose.koinViewModel


@Composable
fun MainScreen(
    onListClick: (Int) -> Unit
) {
    val viewModel: MainScreenViewModel = koinViewModel()
    val mainScreenState by viewModel.state.collectAsState()
    var selectedListForDelete by remember { mutableStateOf<Shoplist?>(null) }

    Scaffold(
        containerColor = LocalCustomColor.current.background,
        topBar = {
            TopBar()
        },
        floatingActionButton = {
            AddingButton(viewModel)
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->

        ShopList(
            viewModel,
            mainScreenState.shoplists,
            Modifier.padding(padding),
            onDeleteRequest = { list ->
                selectedListForDelete = list
                viewModel.obtainEvent(MainScreenEvent.OnDeleteShopListClick)
            },
            onClick = onListClick
        )
    }

    if (mainScreenState.isDialogVisible && selectedListForDelete != null) {
        DeletingAlertWindow(viewModel, selectedListForDelete)
    }

    if (mainScreenState.isDialogAddingItemVisible) {
        DialogAddNameList(
            viewModel = viewModel,
            onListClick = onListClick,
            onDismiss = { viewModel.obtainEvent(MainScreenEvent.OnCloseAddingWindow) }
        )
    }
}

@Composable
private fun DialogAddNameList(
    viewModel: MainScreenViewModel,
    onListClick: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    var newListName by remember { mutableStateOf("") }

    // Подписка на события навигации и скрытия диалога

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ShoppingListEvent.NavigateToIngredients -> {
                    onListClick(event.listId)
                    newListName = ""
                }

                ShoppingListEvent.HideDialog -> {
                    newListName = ""
                }
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Создать новый список", style = LocalTypography.current.h3) },
        text = {
            OutlinedTextField(
                value = newListName,
                onValueChange = { newListName = it },
                label = { Text("Название списка", style = LocalTypography.current.h3) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTypography.current.h3
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (newListName.isNotBlank()) {
                        viewModel.obtainEvent(MainScreenEvent.Add(newListName))
                        viewModel.obtainEvent(MainScreenEvent.OnCloseAddingWindow)
                        viewModel.obtainEvent(MainScreenEvent.Default)
                        newListName = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = LocalCustomColor.current.blueColor
                )
            ) {
                TextLabelWhite(stringResource(R.string.create))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                TextLabelBlue(stringResource(R.string.cancel))
            }
        }
    )
}


@Composable
private fun ShopList(
    viewModel: MainScreenViewModel,
    lists: List<Shoplist>,
    modifier: Modifier = Modifier,
    onDeleteRequest: (Shoplist) -> Unit,
    onClick: (Int) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedList by remember { mutableStateOf<Shoplist?>(null) }

    // Функция для открытия диалога
    val openDialog: (Shoplist) -> Unit = { list ->
        selectedList = list
        showDialog = true
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 28.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(R.string.all_your_lists),
            style = LocalTypography.current.h1,
            color = LocalCustomColor.current.textColor,
            textAlign = TextAlign.Start
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(lists) { list ->
                SwipeableListItem(
                    list = list,
                    onLongPress = { openDialog(list) },
                    onPin = { viewModel.obtainEvent(MainScreenEvent.OnTogglePinListClick(list.id)) },
                    onDelete = { onDeleteRequest(list) },
                    onClick = onClick
                )
                HorizontalDivider()
            }
        }
        Image(
            painter = painterResource(id = R.drawable.shopping_bags),
            contentDescription = null,
            modifier = Modifier
                .padding(bottom = 80.dp)
                .size(250.dp, 114.dp)
        )
    }

    // Диалог
    if (showDialog && selectedList != null) {
        ListOptionsDialog(
            viewModel = viewModel,
            currentName = selectedList!!.shoplistName,
            list = selectedList!!,
            onProcess = {
                showDialog = false
            },
            onDismiss = { showDialog = false },
        )

    }
}


@Composable
fun ListOptionsDialog(
    viewModel: MainScreenViewModel,
    currentName: String,
    list: Shoplist,
    onProcess: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var text by remember { mutableStateOf(currentName) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(text = "Выберите действие", style = LocalTypography.current.h3)

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = {Text(  text = stringResource(R.string.rename_list),
                        style = LocalTypography.current.h4,
                        color = LocalCustomColor.current.textColorCrossed)
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTypography.current.h3
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        onProcess(
                            viewModel.obtainEvent(
                                MainScreenEvent.OnRenameShopListClick(list.id, text)
                            ).toString()
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LocalCustomColor.current.blueColor
                    )
                ) {
                    TextLabelWhite(stringResource(R.string.Confirm))
                }

                Button(
                    onClick = {
                        onProcess(
                            viewModel.obtainEvent(
                                MainScreenEvent.OnDoubleShopListClick(list.id)
                            ).toString()
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LocalCustomColor.current.blueColor
                    )
                ) {
                    TextLabelWhite(stringResource(R.string.make_a_copy))
                }

                TextButton(onClick = { onDismiss() }) {
                    TextLabelBlue(stringResource(R.string.cancel))
                }
            }
        }
    }
}

@Composable
private fun TextLabelTextColor(text: String) {
    Text(
        text = text,
        style = LocalTypography.current.h3,
        color = LocalCustomColor.current.textColor
    )
}

@Composable
private fun TextLabelWhite(text: String) {
    Text(
        text = text,
        style = LocalTypography.current.h3,
        color = LocalCustomColor.current.white
    )
}

@Composable
private fun TextLabelBlue(text: String) {
    Text(
        text = text,
        style = LocalTypography.current.h3,
        color = LocalCustomColor.current.blueColor
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(LocalCustomColor.current.background),
        modifier = Modifier.fillMaxWidth(),
        title = {

        },
        navigationIcon = {
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrowback),
                    contentDescription = stringResource(R.string.back),
                    tint = LocalCustomColor.current.blueColor
                )
                Spacer(modifier = Modifier.width(5.dp))
                TextLabelBlue(stringResource(R.string.back))
            }
        }
    )
}

@Composable
private fun AddingButton(viewModel: MainScreenViewModel) {
    FloatingActionButton(
        onClick = { viewModel.obtainEvent(MainScreenEvent.OnBtnNewShopListClick) },
        shape = CircleShape,
        elevation = FloatingActionButtonDefaults.elevation(0.dp)
    ) {
        Surface(
            modifier = Modifier
                .size(56.dp)
                .border(
                    2.dp,
                    LocalCustomColor.current.blueColor,
                    CircleShape
                ),
            shape = CircleShape,
            color = LocalCustomColor.current.background,
            content = {}
        )

        Icon(
            modifier = Modifier.size(18.dp),
            painter = painterResource(id = R.drawable.add),
            contentDescription = stringResource(R.string.add_list),
            tint = LocalCustomColor.current.blueColor
        )
    }
}

@Composable
private fun DeletingAlertWindow(viewModel: MainScreenViewModel, selectedListForDelete: Shoplist?) {
    AlertDialog(
        onDismissRequest = {
            viewModel.obtainEvent(MainScreenEvent.OnDismissDeleteShopListClick)
        },
        title = {
            TextLabelTextColor(stringResource(R.string.delete_element))
        },
        text = {
            TextLabelTextColor(stringResource(R.string.you_sure_want_delete))
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.obtainEvent(
                        MainScreenEvent.OnDeleteShopListConfirmClick(
                            selectedListForDelete!!.id
                        )
                    )
                    viewModel.obtainEvent(MainScreenEvent.Default)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = LocalCustomColor.current.blueColor
                )
            ) {
                TextLabelWhite(stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    viewModel.obtainEvent(MainScreenEvent.OnDismissDeleteShopListClick)
                }
            ) {
                TextLabelBlue(stringResource(R.string.cancel))
            }
        }
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SwipeableListItem(
    list: Shoplist,
    onLongPress: (Shoplist) -> Unit,
    onPin: () -> Unit,
    onDelete: () -> Unit,
    onClick: (Int) -> Unit
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp)
                .align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    onPin()
                    resetSwipeState()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = LocalCustomColor.current.blueColor
                ),
                modifier = Modifier.size(width = 85.dp, height = 44.dp),
                shape = RoundedCornerShape(0.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    if (list.isPinned) stringResource(R.string.detach) else stringResource(R.string.secure),
                    style = LocalTypography.current.h3
                )
            }
            Button(
                onClick = {
                    onDelete()
                    resetSwipeState()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF44336)
                ),
                modifier = Modifier.size(width = 66.dp, height = 44.dp),
                shape = RoundedCornerShape(0.dp, 0.dp, 10.dp, 0.dp),
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
                                offsetX = -300f
                                isRevealed = true
                            } else {
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
                    .padding(16.dp)
                    .combinedClickable(
                        onClick = { onClick(list.id) },
                        onLongClick = { onLongPress(list) }
                    ),
                verticalAlignment = Alignment.CenterVertically) {

                if (list.isPinned) IconPushed()

                TextLabelTextColor(list.shoplistName)

                Spacer(modifier = Modifier.weight(1f))

                IconOpen()
            }
        }
    }
}

@Composable
private fun IconOpen() {
    Icon(
        painter = painterResource(R.drawable.chevron),
        contentDescription = "Открыть",
        tint = Color.Gray
    )
}

@Composable
private fun IconPushed() {
    Icon(
        painter = painterResource(id = R.drawable.pushpin),
        contentDescription = "Закреплено",
        tint = Color(0xFF2196F3),
        modifier = Modifier.padding(end = 12.dp)
    )
}





