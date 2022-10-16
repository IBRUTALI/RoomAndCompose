package com.example.roomrestore

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.roomrestore.ui.theme.Gold200
import com.example.roomrestore.ui.theme.White200

@Composable
fun ScreenSetup(list: State<List<Item>>, viewModel: MainViewModel, context: Context) {
    val listItem = remember {
        mutableStateOf<List<Item>>(listOf())
    }
    listItem.value = list.value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MainScreen(listItem, viewModel, context)
    }
}

@Composable
fun MainScreen(listItem: MutableState<List<Item>>, viewModel: MainViewModel, context: Context) {
    val openDialog = remember { mutableStateOf(false) }
    val openSortDialog = remember { mutableStateOf(false) }
    Column(
        verticalArrangement = Arrangement.Top
    ) {
        LazyColumnItem(listItem.value)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FloatingActionButton(
                modifier = Modifier.size(40.dp),
                onClick = { openSortDialog.value = true },
                backgroundColor = Gold200
            ) {
                Image(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = R.drawable.ic_baseline_sort_24),
                    contentDescription = "sort_item"
                )
            }
            FloatingActionButton(
                modifier = Modifier.size(40.dp),
                onClick = { openDialog.value = true },
                backgroundColor = Gold200
            ) {
                Image(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = R.drawable.ic_baseline_add_24),
                    contentDescription = "add_item"
                )
            }
        }
        if (openDialog.value) {
            AlertDialogItem(openDialog = openDialog, viewModel = viewModel)
        }

        if (openSortDialog.value) {
            SortDialogItem(openSortDialog = openSortDialog, viewModel = viewModel, listItem = listItem, context = context)
        }
    }
}

@Composable
fun ItemLayout(item: Item) {
    Card(
        shape = RoundedCornerShape(15),
        elevation = 3.dp,
        backgroundColor = White200
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                shape = CircleShape
            ) {
                Image(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = item.image),
                    contentDescription = "image_item"
                )
            }
            Column(
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Text(text = item.title)
                Text(text = item.description.take(10))
            }
        }
    }
}

@Composable
fun AlertDialogItem(openDialog: MutableState<Boolean>, viewModel: MainViewModel) {
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = {
            openDialog.value = false
        },
        title = {
            Text(
                modifier = Modifier.padding(bottom = 5.dp),
                text = stringResource(id = R.string.create_item)
            )
        },
        text = {
            Column {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                    value = title.value,
                    onValueChange = {
                        title.value = it
                    },
                    label = { Text(text = stringResource(R.string.insert_title)) },
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = White200)
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = description.value,
                    onValueChange = {
                        description.value = it
                    },
                    label = { Text(text = stringResource(R.string.insert_description)) },
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = White200)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    openDialog.value = false
                    val item = Item(
                        title = title.value,
                        description = description.value,
                        image = R.drawable.ic_baseline_add_24
                    )
                    viewModel.insertItem(item)
                }) {
                Text(text = stringResource(id = R.string.insert_item))
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    openDialog.value = false
                }) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}

@Composable
fun SortDialogItem(
    openSortDialog: MutableState<Boolean>,
    viewModel: MainViewModel,
    listItem: MutableState<List<Item>>,
    context: Context
) {
    val title = remember { mutableStateOf("") }
    val id = remember { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }
    val dropList = listOf("ID", "Title")
    val selectedIndex = remember { mutableStateOf(0) }
    AlertDialog(
        onDismissRequest = {
            openSortDialog.value = false
        },
        title = {
            Text(
                modifier = Modifier.padding(bottom = 5.dp),
                text = stringResource(id = R.string.find_by)
            )
        },
        text = {
            Column {
                DropDownInit(expanded, dropList, selectedIndex)
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    value = if (selectedIndex.value == 0) id.value
                    else title.value,
                    onValueChange = {
                        if (selectedIndex.value == 0) id.value = it
                        else title.value = it
                    },
                    label = {
                        Text(
                            text = if (selectedIndex.value == 0) {
                                stringResource(R.string.insert_id)
                            } else stringResource(R.string.insert_title)
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = White200),
                    keyboardOptions = if (selectedIndex.value == 0) KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                    else KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    openSortDialog.value = false
                    try {
                            listItem.value = if (selectedIndex.value == 0)
                                viewModel.findItemById(id.value.toInt()).value!!
                            else viewModel.findItemByTitle(title.value).value!!
                    } catch (e: NullPointerException) {
                        Toast.makeText(context, "Объект не найден", Toast.LENGTH_SHORT).show()
                        Log.d("MyLog", "${e.message}")
                    } catch (e: NumberFormatException) {
                        Toast.makeText(context, "Неверный формат ввода данных для поиска", Toast.LENGTH_SHORT).show()
                        Log.d("MyLog", "${e.message}")
                    }

                }) {
                Text(text = stringResource(id = R.string.search))
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    openSortDialog.value = false
                }) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}


@Composable
fun DropDownInit(
    expanded: MutableState<Boolean>,
    dropList: List<String>,
    selectedIndex: MutableState<Int>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.3f)
            .shadow(elevation = 0.dp, shape = RoundedCornerShape(5))
            .padding(start = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = dropList[selectedIndex.value],
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded.value = true }),
            style = TextStyle(
                fontSize = 12.sp,
            ),
            fontWeight = FontWeight.Bold
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier
                .wrapContentWidth()
        ) {
            dropList.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    selectedIndex.value = index
                    expanded.value = false
                }) {
                    Text(text = s)
                }
            }
        }
    }
}

@Composable
fun LazyColumnItem(list: List<Item>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        itemsIndexed(list) { _, item ->
            ItemLayout(item)
        }
    }
}

