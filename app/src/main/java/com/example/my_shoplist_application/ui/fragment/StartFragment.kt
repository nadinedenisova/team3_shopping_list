package com.example.my_shoplist_application.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.my_shoplist_application.R
import com.example.my_shoplist_application.ui.theme.LocalCustomColor
import com.example.my_shoplist_application.ui.theme.LocalTypography
import com.example.my_shoplist_application.ui.theme.My_ShopList_ApplicationTheme
import com.example.my_shoplist_application.ui.viewmodel.ShoppingList
import com.example.my_shoplist_application.ui.viewmodel.ShoppingListViewModel


class StartFragment : Fragment() {
    private val viewModel by viewModels<ShoppingListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                My_ShopList_ApplicationTheme {

                    //   ShoppingListScreen(viewModel = viewModel)

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(/*navController: NavController*/) {
//    val navController = navController
    val viewModel: ShoppingListViewModel = viewModel()
    val lists by viewModel.shoppingLists.collectAsState()
    val isDialogVisible by viewModel.isDialogVisible.collectAsState()
    val isDialogDeleteVisible by viewModel.isDialogDeleteVisible.collectAsState()
    var newListName by remember { mutableStateOf("") }
    var selectedListForDelete by remember { mutableStateOf<ShoppingList?>(null) }

    Scaffold(
        containerColor = LocalCustomColor.current.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(LocalCustomColor.current.background),
                modifier = Modifier.fillMaxWidth(),
                title = {

                },
                navigationIcon = {
                    Row(
                        modifier = Modifier
                            //.clickable(onClick = onBackClick)
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrowback),
                            contentDescription = stringResource(R.string.back),
                            tint = LocalCustomColor.current.blueColor
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(stringResource(R.string.back),
                            style = LocalTypography.current.h3,
                            color = LocalCustomColor.current.blueColor)
                    }
                }
            )
        },
        floatingActionButton = {// кнопка добавить снизу
            FloatingActionButton(
                onClick = { viewModel.showDialog()/*Открыть экран*/ },
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
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->

        ShopList(
            viewModel,
            lists,
            Modifier.padding(padding),
            onDeleteRequest = { list ->
                selectedListForDelete = list
                viewModel.showDeleteDialog()
            }
        )

    }

    if (isDialogDeleteVisible && selectedListForDelete != null) {
        AlertDialog(
            onDismissRequest = {
                viewModel.hideDialog()
            },
            title = {
                Text(
                    text = stringResource(R.string.delete_element),
                    style = LocalTypography.current.h3,
                    color = LocalCustomColor.current.textColor
                )
            },
            text = {
                Text(
                    stringResource(R.string.you_sure_want_delete),
                    style = LocalTypography.current.h3,
                    color = LocalCustomColor.current.textColor
                )
            },
            confirmButton = {
                Button(
                    onClick = { /*удалить элемент */
                        viewModel.deleteList(selectedListForDelete)
                        viewModel.hideDialog()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LocalCustomColor.current.blueColor
                    )
                ) {
                    Text(
                        stringResource(R.string.delete),
                        style = LocalTypography.current.h3,
                        color = LocalCustomColor.current.white
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.hideDialog()
                    }
                ) {
                    Text(
                        stringResource(R.string.cancel),
                        style = LocalTypography.current.h3,
                        color = LocalCustomColor.current.blueColor
                    )
                }
            }
        )
    }

    if (isDialogVisible) {
        AlertDialog(
            onDismissRequest = { viewModel.hideDialog() },
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
                        viewModel.addNewList(newListName)
                        newListName = ""
                        viewModel.hideDialog()
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
}


@Composable
fun ShopList(
    // Показать список
    viewModel: ShoppingListViewModel,
    lists: List<ShoppingList>,
    modifier: Modifier = Modifier,
    onDeleteRequest: (ShoppingList) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedList by remember { mutableStateOf<ShoppingList?>(null) }

    // Функция для открытия диалога
    val openDialog: (ShoppingList) -> Unit = { list ->
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
                    onPin = { viewModel.togglePin(list.id) },
                    onDelete = { onDeleteRequest(list) },
                    onClick = {}
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
            currentName = selectedList!!.name,
            list = selectedList!!,
            onRename = {
                // TODO: обработка переименования
                showDialog = false
            },
            onCopy = {
                // TODO: Обработка копирования
                showDialog = false
            },
            onDismiss = { showDialog = false },
        )
    }
}


@Composable
fun ListOptionsDialog(
    // Диалог при долгом нажатии
    viewModel: ShoppingListViewModel,
    currentName: String,
    list: ShoppingList,
    onRename: (String) -> Unit,
    onCopy: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var text by remember { mutableStateOf(currentName) }
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Выберите действие")

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = {
                        Text(
                            text = stringResource(R.string.rename_list),
                            style = LocalTypography.current.h3,
                            color = LocalCustomColor.current.textColor
                        )
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTypography.current.h3
                )
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onRename(viewModel.renameList(list.id, text).toString()) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LocalCustomColor.current.blueColor
                    )
                ) {
                    Text(
                        "Применить",
                        style = LocalTypography.current.h3,
                        color = LocalCustomColor.current.white
                    )
                }
                Button(
                    onClick = { onCopy(viewModel.addNewList(text).toString()) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LocalCustomColor.current.blueColor
                    )
                ) {
                    Text(
                        "Создать копию",
                        style = LocalTypography.current.h3,
                        color = LocalCustomColor.current.white
                    )
                }
                TextButton(onClick = { onDismiss() }) {
                    Text(
                        stringResource(R.string.cancel),
                        style = LocalTypography.current.h3,
                        color = LocalCustomColor.current.blueColor
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeableListItem(
    list: ShoppingList,
    onLongPress: (ShoppingList) -> Unit,
    onPin: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit
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

        // Основное содержимое элемента
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
                    .padding(16.dp)
                    .combinedClickable(
                        onClick = { onClick /*действие при обычном клике*/ },
                        onLongClick = { onLongPress(list) }  // Долгое нажатие
                    ),
                verticalAlignment = Alignment.CenterVertically,

                ) {
                // Значок закрепления
                if (list.isPinned) {
                    Icon(
                        painter = painterResource(id = R.drawable.pushpin),
                        contentDescription = "Закреплено",
                        tint = Color(0xFF2196F3),
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }

                // Название списка
                Text(
                    text = list.name,
                    style = LocalTypography.current.h3,
                    color = LocalCustomColor.current.textColor
                )

                Spacer(modifier = Modifier.weight(1f))

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


