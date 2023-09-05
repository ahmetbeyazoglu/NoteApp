package com.herpestes.noteapp.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.herpestes.noteapp.database.NoteEntity
import com.herpestes.noteapp.ui.theme.ubuntu

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {

    val notes by viewModel.notes.collectAsState()
    val searchedNotes by viewModel.searchedNotes.collectAsState()

    val (dialogOpen, setDialogOpen) = remember {
        mutableStateOf(false)
    }
    if (dialogOpen) {
        val controller = rememberColorPickerController()
        controller.setWheelRadius(7.dp)
        val (note, setNote) = remember {
            mutableStateOf("")
        }
        val color by animateColorAsState(targetValue = controller.selectedColor.value)

        Dialog(onDismissRequest = { setDialogOpen(false) }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(
                        horizontal = 8.dp,
                        vertical = 12.dp
                    )
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    value = note,
                    onValueChange = {
                        setNote(it)
                    },
                    label = {
                        Text(text = "Note")
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedLabelColor = Color.White,
                        focusedBorderColor = Color.White,
                        focusedTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Note color: ")
                Spacer(modifier = Modifier.height(8.dp))
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    controller = controller,
                    initialColor = Color(0xff2dffc0)
                )
                Spacer(modifier = Modifier.height(10.dp))
                AlphaSlider(
                    controller = controller,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(35.dp),
                    borderRadius = 6.dp,
                    wheelRadius = 7.dp
                )
                Spacer(modifier = Modifier.height(10.dp))
                BrightnessSlider(
                    controller = controller,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(35.dp),
                    borderRadius = 6.dp,
                    wheelRadius = 7.dp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        if (note.isNotEmpty()) {
                            viewModel.addNote(
                                NoteEntity(
                                    note = note,
                                    color = color.toArgb()
                                )
                            )
                            setDialogOpen(false)
                        }
                    },
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        //containerColor = color
                    )
                ) {
                    Text(text = "Add Note", color = Color.White, fontFamily = ubuntu)

                }


            }
        }

    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { setDialogOpen(true) },
                contentColor = Color.White,
                //containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        },
        contentColor = MaterialTheme.colorScheme.primary

    ) { paddings ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = paddings.calculateBottomPadding(),
                    top = 18.dp,
                    start = 12.dp,
                    end = 12.dp
                )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {


            }
            Spacer(modifier = Modifier.height(18.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(androidx.compose.material3.MaterialTheme.colorScheme.secondary)
            )
            Spacer(modifier = Modifier.height(28.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(searchedNotes ?: notes, key = {
                    it.id
                }) { note ->
                    Box(modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(note.color))
                        .fillMaxWidth()
                        .padding(8.dp)
                        .animateItemPlacement(
                            tween(500)
                        )
                        .pointerInput(Unit) {
                            detectTapGestures(onLongPress = { viewModel.deleteNote(note) })
                        }) {
                        Text(text = note.note, color = Color.White)
                    }
                }
            }

        }

    }

}