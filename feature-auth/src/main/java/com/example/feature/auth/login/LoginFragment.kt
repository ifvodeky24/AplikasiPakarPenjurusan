package com.example.feature.auth.login

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.afollestad.vvalidator.form
import com.example.core_data.api.ApiEvent
import com.example.core_data.domain.isGuru
import com.example.core_data.domain.isSiswa
import com.example.core_navigation.ModuleNavigator
import com.example.core_resource.components.base.BaseFragment
import com.example.core_util.bindLifecycle
import com.example.core_util.dismissKeyboard
import com.example.core_util.hideProgress
import com.example.core_util.showProgress
import com.example.feature.auth.AuthViewModel
import com.example.feature_auth.R
import com.example.feature_auth.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate),
    ModuleNavigator {

    private val textBtnLogin by lazy {
        getString(R.string.login)
    }

    private val textHintEmptyEmail by lazy {
        "Email harus diisi"
    }

    private val textHintEmptyPassword by lazy {
        "Password harus diisi"
    }

    private val viewModel by sharedViewModel<AuthViewModel>()

    override fun initView() {
        setupInput()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            }
        )

        binding.tvGuruBk.setOnClickListener {
            val directionRegister =
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment("guru")
            findNavController().navigate(directionRegister)
        }

        binding.tvMurid.setOnClickListener {
            val directionRegister =
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment("siswa")
            findNavController().navigate(directionRegister)
        }
    }

    override fun initListener() {

    }

    private fun setupInput() {
        with(binding) {
            form {
                useRealTimeValidation(disableSubmit = true)
                inputLayout(R.id.til_username) {
                    isNotEmpty().description(textHintEmptyEmail)
                }
                inputLayout(R.id.til_password) {
                    isNotEmpty().description(textHintEmptyPassword)
                }
                submitWith(R.id.btn_login) {
                    dismissKeyboard()
                    val username = edtUsername.text.toString()
                    val password = edtPassword.text.toString()

                    viewModel.username = username
                    viewModel.password = password

                    viewModel.login(username, password)

                    viewModel.loginRequest.observe(viewLifecycleOwner, { login ->
                        when (login) {
                            is ApiEvent.OnProgress -> {
                                showProgress()
                                Timber.d("progress ${login.currentResult}")
                            }
                            is ApiEvent.OnSuccess -> {
                                if (login.hasNotBeenConsumed) {
                                    login.getData(true)
                                    login.getData()?.let {
                                        if (it.isGuru) {
                                            viewModel.getGuruById(it.idUser.toInt())

                                            viewModel.guruRequest.observe(
                                                viewLifecycleOwner,
                                                { guru ->
                                                    when (guru) {
                                                        is ApiEvent.OnProgress -> {
                                                            showProgress()
                                                            Timber.d("progress ${guru.currentResult}")
                                                        }
                                                        is ApiEvent.OnSuccess -> {
                                                            hideProgress(true)
                                                            navigateToHomeActivity(finishCurrent = true)
                                                        }
                                                        is ApiEvent.OnFailed -> {
                                                            hideProgress(true)
                                                            Snackbar.make(
                                                                requireContext(),
                                                                requireView(),
                                                                guru.getException().toString(),
                                                                Snackbar.LENGTH_SHORT
                                                            ).show()
                                                            Log.d(
                                                                "sdsdsd",
                                                                "cureesdsd ${
                                                                    guru.getException().toString()
                                                                }"
                                                            )
                                                        }
                                                    }
                                                })
                                        } else if (it.isSiswa) {
                                            viewModel.getSiswaById(it.idUser.toInt())

                                            viewModel.siswaRequest.observe(
                                                viewLifecycleOwner,
                                                { siswa ->
                                                    when (siswa) {
                                                        is ApiEvent.OnProgress -> {
                                                            showProgress()
                                                            Timber.d("progress ${siswa.currentResult}")
                                                        }
                                                        is ApiEvent.OnSuccess -> {
                                                            hideProgress(true)
                                                            navigateToHomeActivity(finishCurrent = true)
                                                        }
                                                        is ApiEvent.OnFailed -> {
                                                            hideProgress(true)
                                                            Snackbar.make(
                                                                requireContext(),
                                                                requireView(),
                                                                siswa.getException().toString(),
                                                                Snackbar.LENGTH_SHORT
                                                            ).show()
                                                            Log.d(
                                                                "sdsdsd",
                                                                "cureesdsd ${
                                                                    siswa.getException().toString()
                                                                }"
                                                            )
                                                        }
                                                    }
                                                })
                                        }
                                    }
                                }

                            }
                            is ApiEvent.OnFailed -> {
                                hideProgress(true)
                                Snackbar.make(
                                    requireContext(), requireView(),
                                    login.getException().toString(), Snackbar.LENGTH_SHORT
                                ).show()
                                Log.d("sdsdsd", "cureesdsd ${login.getException().toString()}")

                            }
                        }
                    })
                }
            }
            btnLogin.bindLifecycle(viewLifecycleOwner)
        }
    }

    private fun showProgress() = with(binding) {
        listOf(
            btnLogin, tilUsername, tilPassword,
            edtUsername, edtPassword,
        ).forEach { it.isEnabled = false }

        btnLogin.showProgress()
    }

    private fun hideProgress(isEnable: Boolean) = with(binding) {
        btnLogin.postDelayed(
            {
                listOf(
                    btnLogin, tilUsername, tilPassword,
                    edtUsername, edtPassword,
                ).forEach { it.isEnabled = true }
            }, 1000L
        )

        btnLogin.hideProgress(textBtnLogin) {
            isEnable && with(binding) {
                "${edtUsername.text}".isNotBlank() && "${edtPassword.text}".isNotBlank()
            }
        }
    }
}