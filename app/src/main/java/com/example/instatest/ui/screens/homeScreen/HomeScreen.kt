package com.example.instatest.ui.screens.homeScreen

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.instatest.R
import com.example.instatest.core.enums.Methods
import com.example.instatest.core.enums.RequestBodyType
import com.example.instatest.core.utils.Constants.minTwoCharsRegex
import com.example.instatest.core.utils.Constants.urlRegex
import com.example.instatest.core.utils.validateJSON
import com.example.instatest.ui.BodyText3Text
import com.example.instatest.ui.CustomDropDown
import com.example.instatest.ui.HeadLine2Text
import com.example.instatest.ui.Headline3
import com.example.instatest.ui.OutlinedButtonPurple
import com.example.instatest.ui.TextInputWithLabel
import com.example.instatest.ui.screens.homeScreen.states.HomeUiEvents
import com.example.instatest.ui.screens.homeScreen.states.RequestHeader
import com.example.instatest.ui.theme.LilacPetals
import com.example.instatest.ui.theme.Violet
import com.example.instatest.core.utils.validateWithRegex
import com.example.instatest.core.utils.NetworkChecker
import com.example.instatest.ui.ButtonTextTwo
import com.example.instatest.ui.Dialog
import com.example.instatest.ui.ErrorComponent
import com.example.instatest.ui.LargeTextDisplay
import com.example.instatest.ui.navigation.Routes.HISTORY_SCREEN
import com.example.instatest.ui.screens.homeScreen.states.NetworkState
import com.example.instatest.ui.showToast
import com.example.instatest.ui.theme.Alert
import com.example.instatest.ui.theme.PurplePlum
import com.example.instatest.ui.theme.VioletLight
import java.io.File


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel) {
    val (showDialog, setShowDialog) = rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val connectivityManager =
        context.getSystemService(ConnectivityManager::class.java)
    val networkChecker by lazy { NetworkChecker(connectivityManager) }
    Scaffold(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize(),
        floatingActionButton = {

            ExtendedFloatingActionButton(
                text = {
                    ButtonTextTwo(text = stringResource(R.string.hit), color = PurplePlum)
                },
                icon = { ButtonTextTwo(text = "🔨") },
                onClick = {
                    if (viewModel.isValidScreen()) {
                        networkChecker.performAction({
                            viewModel.hitTheApi()
                            setShowDialog(true)
                        }) {
                            showToast(context,
                               context. getString(R.string.please_check_your_internet_connection)
                            )
                        }
                    }
                },
                expanded = viewModel.isValidScreen(),
                containerColor = VioletLight,
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.padding(16.dp)
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) {
        Content(navController, viewModel, showDialog) { show ->
            setShowDialog(show)
        }
    }
}

@Composable
private fun Content(
    navController: NavHostController,
    viewModel: HomeViewModel,
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            viewModel.onEvent(HomeUiEvents.SelectFile(it))
        }
    )
    val uiState = viewModel.uiState.value
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.SpaceBetween) {
            HeadLine2Text(stringResource(R.string.test_your_api))
            IconButton(
                onClick = { navController.navigate(HISTORY_SCREEN) },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.history),
                    contentDescription = stringResource(id = R.string.history)
                )
            }
        }
        TextInputWithLabel(
            Modifier
                .wrapContentWidth()
                .fillMaxWidth(),
            labelColor = Color.DarkGray,
            placeholder = stringResource(R.string.enter_your_url_here),
            default = uiState.url,
            error = uiState.url.validateWithRegex(
                urlRegex,
                stringResource(R.string.please_enter_a_valid_url)
            ),
            leadingIcon = R.drawable.url,
        ) {
            viewModel.onEvent(HomeUiEvents.UrlEntered(it))
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CustomDropDown(
                Modifier
                    .wrapContentSize()
                    .fillMaxWidth()
                    .weight(1f),
                label = uiState.requestType?.name
                    ?: stringResource(R.string.method),
                list = viewModel.methodsList.map { it.name }) {
                if (it >= 0) viewModel.onEvent(HomeUiEvents.MethodSelected(viewModel.methodsList[it]))
            }
            AnimatedVisibility(
                visible = uiState.requestType == Methods.POST,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Row {
                    Spacer(modifier = Modifier.width(4.dp))
                    CustomDropDown(label = uiState.requestBodyType?.name
                        ?: stringResource(R.string.request_type),
                        list = viewModel.requestBodyTypeList.map { it.name }) {
                        if (it >= 0) viewModel.onEvent(
                            HomeUiEvents.RequestBodyTypeSelected(
                                viewModel.requestBodyTypeList[it]
                            )
                        )
                    }
                }
            }

        }

        AnimatedVisibility(
            visible = uiState.requestBodyType == RequestBodyType.MULTIPART,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                TextInputWithLabel(
                    Modifier
                        .wrapContentWidth()
                        .fillMaxWidth(),
                    labelColor = Color.DarkGray,
                    placeholder = stringResource(R.string.enter_the_key),
                    default = uiState.formKey ?: "",
                    error = uiState.formKey?.validateWithRegex(
                        minTwoCharsRegex,
                        stringResource(R.string.please_a_valid_key)
                    ),
                ) {
                    viewModel.onEvent(HomeUiEvents.AddFromDataKey(it))
                }
                (if (uiState.fileUri != null) uiState.fileUri?.path?.let {
                    File(
                        it
                    ).name
                } else stringResource(R.string.upload_file))?.let {
                    OutlinedButtonPurple(
                        modifier = Modifier.fillMaxWidth(),
                        text = it
                    ) {
                        launcher.launch("*/*")
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = uiState.requestBodyType == RequestBodyType.JSON,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextInputWithLabel(
                Modifier
                    .wrapContentWidth()
                    .fillMaxWidth(),
                labelColor = Color.DarkGray,
                placeholder = stringResource(R.string.enter_json_body),
                default = uiState.jsonBody ?: "",
                error = uiState.jsonBody?.validateJSON(stringResource(R.string.please_enter_a_valid_json)),
                leadingIcon = R.drawable.json,
                10
            ) {
                viewModel.onEvent(HomeUiEvents.AddJSONBody(it))
            }
        }
        Headline3(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.request_headers),
            textAlign = TextAlign.Start
        )
        Row(verticalAlignment = Alignment.Top) {
            TextInputWithLabel(
                Modifier
                    .wrapContentSize()
                    .fillMaxWidth()
                    .weight(3f),
                labelColor = Color.DarkGray,
                placeholder = stringResource(R.string.header),
                default = uiState.headerKey ?: "",
                error = null,
                leadingIcon = R.drawable.key,
            ) {
                viewModel.onEvent(HomeUiEvents.AddHeaderKey(it))
            }
            Spacer(modifier = Modifier.width(4.dp))
            TextInputWithLabel(
                Modifier
                    .wrapContentSize()
                    .fillMaxWidth()
                    .weight(3f),
                labelColor = Color.DarkGray,
                placeholder = stringResource(R.string.value),
                default = uiState.headerValue,
                error = null,
                leadingIcon = R.drawable.value,
            ) {
                viewModel.onEvent(HomeUiEvents.AddHeaderValue(it))
            }
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(
                onClick = {
                    viewModel.onEvent(
                        HomeUiEvents.AddRequestHeader(
                            RequestHeader(
                                uiState.headerKey!!, uiState.headerValue
                            )
                        )
                    )
                },
                Modifier
                    .weight(1f)
                    .padding(top = 4.dp), enabled = uiState.headerKey?.isNotEmpty() ?: false
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add),
                    tint = Violet
                )
            }
        }
        uiState.headersList.forEachIndexed { index, item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(LilacPetals, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BodyText3Text(
                    text = "\"${item.key}\" : \"${item.value}\"",
                    textAlign = TextAlign.Start, modifier = Modifier.weight(5f)
                )
                IconButton(onClick = {
                    viewModel.onEvent(
                        HomeUiEvents.RemoveRequestHeader(index)
                    )
                }, Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete),
                        tint = Alert
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
    if (showDialog)
        Dialog(
            {
                setShowDialog(false)
            },
            {
                setShowDialog(false)
            },
        ) {
            val state by viewModel.apiResponse.observeAsState()
            when (state) {
                is NetworkState.Loading -> {
                    Box(
                        Modifier
                            .padding(50.dp)
                            .fillMaxWidth()
                    )
                    { Headline3(text = "Loading...") }
                }

                is NetworkState.Success -> {
                    val successState = (state as NetworkState.Success)
                    HeadLine2Text(
                        text = stringResource(R.string.overview),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(LilacPetals, shape = RoundedCornerShape(8.dp))
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BodyText3Text(text = successState.data.overview.toString())
                    }
                    var e by remember {
                        mutableStateOf(false)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        HeadLine2Text(
                            text = stringResource(R.string.request),
                            modifier = Modifier
                                .clickable { e = e.not() }
                            ,
                            textAlign = TextAlign.Start,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = {
                            e=e.not()
                        }, ) {
                            Icon(
                                painter = painterResource(id =if (e) R.drawable.icon_expand else R.drawable.arrow_down),
                                contentDescription = null,
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(LilacPetals, shape = RoundedCornerShape(8.dp))
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimatedVisibility(visible = e) {
                            LargeTextDisplay(text = if (successState.data.request != null) successState.data.request.toString() else "Empty Request")
                        }
                    }
                    HeadLine2Text(
                        text = stringResource(R.string.response),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(LilacPetals, shape = RoundedCornerShape(8.dp))
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BodyText3Text(text = if (successState.data.response != null) successState.data.response.toString() else "Empty Response")
                    }
                }

                else -> {
                    ErrorComponent {
                        viewModel.hitTheApi()
                    }
                }
            }
        }
}


@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    HomeScreen(navController = rememberNavController(), viewModel = viewModel())
}