package com.example.kgapp

interface MsgAsyncCallback {
    fun onPreExecute()
    fun onPostExecute(text: String)
}