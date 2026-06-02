package com.example.standardapp.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class SampleDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Sample Dialog")
            .setMessage("This is a DialogFragment example.")
            .setPositiveButton("OK", null)
            .create()
    }
}
