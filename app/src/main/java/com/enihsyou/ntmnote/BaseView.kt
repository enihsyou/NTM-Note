package com.enihsyou.ntmnote

interface BaseView<T : BasePresenter> {

    var presenter: T
}
