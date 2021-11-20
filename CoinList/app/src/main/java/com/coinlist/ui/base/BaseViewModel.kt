package com.coinlist.ui.base

import androidx.lifecycle.ViewModel
import com.coinlist.domain.usecase.BaseUseCase

abstract class BaseViewModel(protected val baseUseCase: BaseUseCase) : ViewModel()
