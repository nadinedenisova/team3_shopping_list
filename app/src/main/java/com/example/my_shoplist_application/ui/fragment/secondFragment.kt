package com.example.my_shoplist_application.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.my_shoplist_application.R
import com.example.my_shoplist_application.ui.theme.LocalCustomColor
import com.example.my_shoplist_application.ui.theme.LocalTypography
import com.example.my_shoplist_application.ui.theme.My_ShopList_ApplicationTheme
import com.example.my_shoplist_application.ui.viewmodel.SecondScreenViewModel
import com.example.my_shoplist_application.ui.viewmodel.ShoppingList
import com.example.my_shoplist_application.ui.viewmodel.ShoppingListItem

class secondFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                My_ShopList_ApplicationTheme {
//                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                      //  Greeting(
////                            name = "Android",
////                            modifier = Modifier.padding(innerPadding)
////                        )
//                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(viewModel: SecondScreenViewModel) {
    val items by viewModel.items.collectAsState()
    val suggestedNames by viewModel.suggestedNames.collectAsState()
    var newItemName by remember { mutableStateOf("") }
    var newItemQuantity by remember { mutableStateOf(1) }
    var newItemUnit by remember { mutableStateOf<String?>(null) }
    var showContextMenu by remember { mutableStateOf(false) }
    val lists by viewModel.shoppingLists.collectAsState()
    val isChecked by viewModel.switchStatus.collectAsState()

    Scaffold(
        containerColor = LocalCustomColor.current.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(LocalCustomColor.current.background),
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        "Byuhblbtyns",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
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
                        Text(
                            stringResource(R.string.back),
                            style = LocalTypography.current.h3,
                            color = LocalCustomColor.current.blueColor
                        )
                    }
                },
                actions = {
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
            )
        },
        floatingActionButton = {// кнопка добавить снизу
            FloatingActionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                onClick = { /*viewModel.showDialog()*//*Открыть экран*/ },
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
        ShopList2(viewModel, lists, Modifier.padding(padding), isChecked)
    }
}

@Composable
fun ShopList2(
    // Показать список
    viewModel: SecondScreenViewModel,
    lists: List<ShoppingListItem>,
    modifier: Modifier = Modifier,
    //  onDeleteRequest: (ShoppingList) -> Unit,
    isChecked: Boolean,
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
        SwitchWithText(isChecked) { isChecked -> viewModel.switch(isChecked) }

        LazyColumn() {
            items(lists, key = { it.id }) { item ->
                SwipeableItem(
                    viewModel,
                    item = item,
                    onDelete = {},
                    onClick = {},
                )
                HorizontalDivider(Modifier.padding(start = 64.dp))

            }
        }

        TextButton(
            modifier = Modifier.align(Alignment.Start),
            onClick = {/*Открыть диалог Добавить продукт*/ },
        ) {
            Text(
                "Добавить продукт",
                Modifier.padding(start = 16.dp, top = 20.dp),
                style = LocalTypography.current.h3,
                color = LocalCustomColor.current.blueColor
            )
        }
    }

    // Диалог
//    if (showDialog && selectedList != null) {
//        ListOptionsDialog(
//            viewModel = viewModel,
//            currentName = selectedList!!.name,
//            list = selectedList!!,
//            onRename = {
//                // TODO: обработка переименования
//                showDialog = false
//            },
//            onCopy = {
//                // TODO: Обработка копирования
//                showDialog = false
//            },
//            onDismiss = { showDialog = false },
//        )
//    }
}

@Composable
fun SwipeableItem(
    viewModel: SecondScreenViewModel,
    item: ShoppingListItem,
 //   onSwipeToDelete: () -> Unit,
 //   onSwipeToEdit: () -> Unit,
 //   onPin: () -> Unit,
    onDelete: () -> Unit,
   // content: @Composable (() -> Unit),
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
               // .padding(vertical = 8.dp, horizontal = 12.dp)
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
                    checked = item.isPurchased,
                    onCheckedChange = { viewModel.togglePurchased(item.id) }
                )
                Text(
                    text = "${item.name} ", Modifier.padding(start = 16.dp),
                    style = LocalTypography.current.h3,
                    color = LocalCustomColor.current.textColor,
                )
                Spacer(modifier = Modifier.weight(1f))

                Text(modifier = Modifier.padding(end = 12.dp) ,
                    text = " ${item.quantity} ${item.unit.label}",
                    style = LocalTypography.current.h3,
                    color = LocalCustomColor.current.textColorLabe,
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
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
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

// Preview
@Composable
fun PreviewShoppingListScreen() {
    val viewModel = SecondScreenViewModel()
    ShoppingListScreen(viewModel = viewModel)
}


