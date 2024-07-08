package com.codebook.instatest.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Arrangement.End
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.codebook.instatest.core.utils.splitTextIntoChunks
import com.codebook.instatest.data.database.model.NetworkCacheEntry
import com.codebook.instatest.ui.theme.Alert
import com.codebook.instatest.ui.theme.BabyBlueLight
import com.codebook.instatest.ui.theme.ColdGrey
import com.codebook.instatest.ui.theme.DeepBlue
import com.codebook.instatest.ui.theme.LilacPetals
import com.codebook.instatest.ui.theme.LilacPetalsDark
import com.codebook.instatest.ui.theme.Peach
import com.codebook.instatest.ui.theme.PurplePlum
import com.codebook.instatest.ui.theme.Violet
import com.codebook.instatest.ui.theme.VioletLight
import com.codebook.instatest.ui.theme.Water
import com.codebook.instatest.R



@Composable
fun OutlinedButtonPurple(
    modifier: Modifier = Modifier,
    buttonModifier: Modifier = Modifier,
    text: String,
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = { onClick() },
        border = BorderStroke(1.dp, PurplePlum),
        modifier = buttonModifier.wrapContentSize(), enabled = isEnabled
    ) {
        ButtonTextTwo(text = text, modifier = modifier, color = PurplePlum)
    }
}

@Composable
fun TextInputWithLabel(
    modifier: Modifier = Modifier,
    labelColor: Color = ColdGrey,
    placeholder: String = "",
    default: String,
    error: String?,
    leadingIcon: Int? = null,
    maxLines: Int = 1,
    onValueChange: (String) -> Unit
) {
    var value by remember {
        mutableStateOf(default)
    }
    value = default
    val localFocusManager = LocalFocusManager.current
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = {
            value = it
            onValueChange(it)
        }, maxLines = maxLines,
        isError = !error.isNullOrEmpty(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = LilacPetals,
            unfocusedContainerColor = LilacPetals,
            disabledContainerColor = LilacPetals,
            errorContainerColor = LilacPetals,
            errorCursorColor = Alert,
            focusedIndicatorColor = Violet,
            unfocusedIndicatorColor = LilacPetalsDark,
            errorIndicatorColor = Alert,
            errorTrailingIconColor = Alert,
            errorSupportingTextColor = Alert,
        ),
        placeholder = {
            BodyTextTwo(text = placeholder, color = labelColor)
        },
        shape = RoundedCornerShape(20.dp),
        supportingText = {
            if (!error?.trim().isNullOrEmpty()) BodyText3Text(
                text = error.toString(),
                color = Alert,
                textAlign = TextAlign.Start
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium.copy(labelColor),
        singleLine = maxLines == 1,
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()
        },

        leadingIcon = {
            if (leadingIcon != null) {
                Icon(
                    painter = painterResource(id = leadingIcon),
                    contentDescription = null,
                    tint = Violet
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done, keyboardType = KeyboardType.Uri
        )
    )
}

@Composable
fun CustomDropDown(
    modifier: Modifier = Modifier, label: String,
    list: List<String>,
    onChange: (Int) -> Unit
) {
    var mExpanded by remember { mutableStateOf(false) }
    var selectedElement by remember { mutableStateOf(label) }
    LaunchedEffect(key1 = selectedElement) {
        onChange(list.indexOf(selectedElement))
    }
    Box(
        modifier = modifier
            .background(LilacPetals, RoundedCornerShape(20.dp))
            .wrapContentHeight()
            .border(
                1.dp,
                color = if (mExpanded) PurplePlum else LilacPetalsDark,
                RoundedCornerShape(20.dp)
            )

    ) {
        Column(
            Modifier.padding(vertical = 13.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            Row(
                Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { mExpanded = !mExpanded }
                    .padding(vertical = 5.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BodyTextTwo(text = selectedElement)
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )
                if (mExpanded)
                    Icon(
                        painter = painterResource(id = R.drawable.icon_collapse),
                        contentDescription = null, tint = Color.Unspecified
                    ) else Icon(
                    painter = painterResource(id = R.drawable.arrow_down),
                    contentDescription = null, tint = Color.Unspecified
                )
            }
            AnimatedVisibility(visible = mExpanded) {
                Column(
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(13.dp)
                ) {
                    list.forEach { s ->
                        HDivider(color = Violet)
                        BodyTextTwo(
                            text = s,
                            color = DeepBlue,
                            textAlign = TextAlign.Start,
                            modifier = if (s == selectedElement) Modifier
                                .background(
                                    VioletLight,
                                    RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                            else
                                Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable {
                                        selectedElement = s
                                        mExpanded = !mExpanded
                                    }
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BodyText3Text(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = ColdGrey,
    textAlign: TextAlign = TextAlign.Center,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall.copy(color = color),
        textAlign = textAlign,
        modifier = modifier,
    )
}

@Composable
fun HeadLine2Text(
    text: String, modifier: Modifier = Modifier,
    color: Color = ColdGrey,
    textAlign: TextAlign = TextAlign.Center,

    ) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium.copy(color = color),
        textAlign = textAlign,
        modifier = modifier,
    )
}

@Composable
fun Headline3(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = ColdGrey,
    textAlign: TextAlign = TextAlign.Center,

    ) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall.copy(color = color),
        modifier = modifier,
        textAlign = textAlign
    )
}

@Composable
fun HDivider(modifier: Modifier = Modifier, color: Color = Color.LightGray) {
    Box(
        modifier = modifier
            .background(color)
            .height(1.dp)
            .fillMaxWidth()
    )
}

@Composable
fun BodyTextTwo(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = ColdGrey,
    textAlign: TextAlign = TextAlign.Center,

    ) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium.copy(color = color),
        textAlign = textAlign,
        modifier = modifier,
    )
}

@Composable
fun ButtonTextTwo(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = ColdGrey,
    textAlign: TextAlign = TextAlign.Center,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium.copy(color = color),
        textAlign = textAlign,
        modifier = modifier,
    )
}

@Composable
fun Dialog(
    onDismissRequest: () -> Unit,
    onX: () -> Unit,
    content: @Composable () -> Unit
) {
    androidx.compose.ui.window.Dialog(
        onDismissRequest = { onDismissRequest() }, DialogProperties(
            dismissOnBackPress = false,
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(), color = PurplePlum.copy(0.4f)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp), verticalArrangement = Center
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = End
                    ) {
                        IconButton(modifier = Modifier.size(48.dp), onClick = { onX() }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.close),
                                tint = White
                            )
                        }

                    }
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 40.dp, spotColor = PurplePlum, ambientColor = PurplePlum
                            )
                            .background(White, RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Column(
                            Modifier.fillMaxSize(),
                            verticalArrangement = SpaceEvenly,
                            horizontalAlignment = CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(20.dp))
                            content()
                            Spacer(modifier = Modifier.height(20.dp))
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun LargeTextDisplay(text: String, chunkSize: Int = 500) {
    val textChunks = remember(text, chunkSize) {
        text.splitTextIntoChunks(chunkSize)
    }

    Column {
        for (chunk in textChunks) {
            BodyText3Text(text = chunk, textAlign = TextAlign.Justify)
        }
    }
}

@Composable
fun ErrorComponent(retry: () -> Unit) {
    Column(
        Modifier
            .wrapContentSize()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            modifier = Modifier.size(150.dp),
            tint = Alert
        )
        HeadLine2Text(
            text = "Oh NO!",
        )
        Headline3(
            text = "May be bigfoot has broken this page.\n Try Again! ",
        )
        Button(
            onClick = { retry() }, modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Retry !")
        }

    }

}

@Composable
fun DropdownMenu(
    selected: String,
    onSelectedChange: (String) -> Unit,
    items: List<String> = listOf(
        stringResource(R.string.timestamp), stringResource(id = R.string.method),
        stringResource(
            R.string.status
        )
    )
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        BodyText3Text(text = selected, modifier = Modifier.clickable { expanded = true })
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            items.forEach { item ->
                DropdownMenuItem(text = { BodyText3Text(text = item) }, onClick = {
                    onSelectedChange(item)
                    expanded = false
                })
            }
        }
    }
}

@Composable
fun CachedCallItem(call: NetworkCacheEntry) {
    var e by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .also {
                if (e)
                    it.wrapContentHeight()
                else
                    it.height(200.dp)
            }
            .clickable { e = e.not() }
            .padding(8.dp)
            .background(getCardColor(call), shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
    ) {
        BodyText3Text(text = "URL: ${call.url}")
        if (call.query.isNullOrEmpty().not())
            BodyText3Text(text = "Query Prams: ${call.query}")
        BodyText3Text(text = "Method: ${call.method}")
        if (call.responseCode != 0)
            BodyText3Text(text = "Response Code: ${call.responseCode}")
        if (call.headers.isNullOrEmpty().not() && call.headers.equals("{}").not())
            BodyText3Text(text = "Request Headers: ${call.headers}")
        if (call.requestBody.isNullOrEmpty().not())
            BodyText3Text(text = "Request Body: ${call.requestBody}")
        AnimatedVisibility(visible = e) {
            Column {
                if (call.responseHeaders.isNullOrEmpty().not() && call.responseHeaders.equals("{}")
                        .not()
                )
                    BodyText3Text(text = "Response Headers: ${call.responseHeaders}")
                BodyText3Text(text = "Response Body: ${call.responseBody}")
                BodyText3Text(text = "Timestamp: ${call.timestamp}")
            }
        }
    }
}

fun getCardColor(call: NetworkCacheEntry): Color {
    when (call.responseCode) {
        in 200..299 -> {
            return Water
        }

        in 400..499 -> {
            return Alert
        }

        in 500..599 -> {
            return Peach
        }

        else -> {
            return BabyBlueLight
        }
    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}