package com.example.my_shoplist_application.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

                    ShoppingListScreen(viewModel = viewModel)

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(viewModel: ShoppingListViewModel) {
    val lists by viewModel.shoppingLists.collectAsState()
    val isDialogVisible by viewModel.isDialogVisible.collectAsState()
    var newListName by remember { mutableStateOf("") }

    Scaffold(
        containerColor = LocalCustomColor.current.background,
        topBar = {
            TopAppBar(
                colors =  TopAppBarDefaults.topAppBarColors(LocalCustomColor.current.background),
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Modifier.padding(start =5.dp)
                    Text(
                        "Назад",
                        style = LocalTypography.current.h2,
                        color = LocalCustomColor.current.blueColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Навигация назад */ }) {
                        Icon(
                            painter = painterResource(R.drawable.arrowback),
                            contentDescription = "Назад",
                            tint = LocalCustomColor.current.blueColor
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showDialog() },
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(0.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .size(56.dp)
                        .border(2.dp, LocalCustomColor.current.blueColor, CircleShape),  // Толщина, цвет и форма контура
                    shape = CircleShape,
                    color = LocalCustomColor.current.background,  // Чтобы контур был виден, фон должен быть прозрачным
                    content = {}
                )

                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = "Добавить список",
                    tint = LocalCustomColor.current.blueColor
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        HistoryTrackList(viewModel,lists, Modifier.padding(padding)) { }

    }

    // Диалог для добавления нового списка
    if (isDialogVisible) {
        AlertDialog(
            onDismissRequest = { viewModel.hideDialog() },
            title = { Text("Создать новый список", style = LocalTypography.current.h2) },
            text = {
                OutlinedTextField(
                    value = newListName,
                    onValueChange = { newListName = it },
                    label = { Text("Название списка", style = LocalTypography.current.h2) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTypography.current.h2
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
                    Text("Создать", style = LocalTypography.current.h2)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideDialog() }) {
                    Text("Отмена", style = LocalTypography.current.h2)
                }
            }
        )
    }
}
@Composable
fun HistoryTrackList(
    viewModel: ShoppingListViewModel,
    lists: List<ShoppingList>,
    modifier: Modifier = Modifier,
    function: () -> Unit
//                       shoppingList: List<ShoppingList>,
//                      onClick: (ShoppingList) -> Unit,
//                      onClearHistory: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding( top = 28.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Все ваши списки ",
            style = LocalTypography.current.h1,
            color = LocalCustomColor.current.textColor,
            textAlign = TextAlign.Start
        )

        Column(
            modifier = Modifier
                .padding(top = 28.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                // Закрепленные элементы (не скроллятся)
                val pinnedLists = lists.filter { it.isPinned }
                items(pinnedLists) { list ->
                    SwipeableListItem(
                        list = list,
                        onPin = { viewModel.togglePin(list.id) },
                        onDelete = { viewModel.deleteList(list.id) }
                    )
                    HorizontalDivider()
                }
                // Обычные элементы (скроллятся)
                val unpinnedLists = lists.filter { !it.isPinned }
                items(unpinnedLists) { list ->
                    SwipeableListItem(
                        list = list,
                        onPin = { viewModel.togglePin(list.id) },
                        onDelete = { viewModel.deleteList(list.id) }
                    )
                    HorizontalDivider()
                }
            }
            Image(
                painter = painterResource(id = R.drawable.shopping_bags),
                contentDescription = null,
                modifier = Modifier
                    .padding(bottom = 80.dp)
                    .size(250.dp,114.dp)
            )
        }
    }
}

@Composable
fun SwipeableListItem(
    list: ShoppingList,
    onPin: () -> Unit,
    onDelete: () -> Unit
) {
    var isRevealed by remember { mutableStateOf(false) }
    var offsetX by remember { mutableStateOf(0f) }

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
                    containerColor = Color(0xFF2196F3)
                ),
                modifier = Modifier.size(width = 85.dp, height = 44.dp),
                shape = RoundedCornerShape(0.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(if (list.isPinned) "Открепить" else "Закрепить", style = LocalTypography.current.h2)
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
                shape = RoundedCornerShape(0.dp,0.dp,10.dp,0.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Удалить", style = LocalTypography.current.h2)
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
                    .padding(16.dp),
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
                    style = LocalTypography.current.h2,
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