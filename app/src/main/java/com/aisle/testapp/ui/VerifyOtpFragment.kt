package com.aisle.testapp.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aisle.testapp.R
import com.aisle.testapp.databinding.FragmentVerifyOtpBinding
import com.aisle.testapp.others.AppConstants
import com.aisle.testapp.others.NetworkResult
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VerifyOtpFragment : Fragment(), View.OnClickListener {
    private lateinit var countryCode: String
    private lateinit var mobileNumber: String

    private lateinit var binding: FragmentVerifyOtpBinding
    private val loginViewModel by viewModels<LoginViewModel>()
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            countryCode = it.getString(AppConstants.COUNTRY_CODE).toString()
            mobileNumber = it.getString(AppConstants.MOBILE_NUM).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerifyOtpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvMobNum.text = countryCode.plus(mobileNumber)
        binding.ivEditNum.setOnClickListener(this)
        binding.btnContinue.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnContinue -> {
                verifyOtp()
            }
            R.id.ivEditNum -> {
                findNavController().popBackStack()
            }
        }
    }

    private fun verifyOtp() {
        val otp = binding.etOtp.text.toString().trim()
        if(otp.isEmpty()){
            Toast.makeText(requireContext(),"Please enter OTP",Toast.LENGTH_LONG).show()
            return
        }
        val jsonObject = JsonObject()
        jsonObject.addProperty("number", countryCode.plus(mobileNumber))
        jsonObject.addProperty("otp",otp)
        loginViewModel.verifyOtp(jsonObject)

        loginViewModel.verifyOtpResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Failure -> {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(requireContext(),it.errorMessage, Toast.LENGTH_LONG).show()
                }
                is NetworkResult.Loading -> {
                    hideKeyBoard()
                    binding.progress.visibility = View.VISIBLE
                }
                is NetworkResult.Success -> {
                    binding.progress.visibility = View.GONE
                    val result = it.data as JsonObject
                    if(!result.get("token").isJsonNull){
                        val token = result.get("token").asString
                        val editor = sharedPreferences.edit()
                        editor.putBoolean(AppConstants.USER_LOGGED_IN,true)
                        editor.putString(AppConstants.USER_MOB_NUM,mobileNumber)
                        editor.putString(AppConstants.USER_AUTH_TOKEN,token)
                        editor.apply()
                        findNavController().navigate(R.id.action_verifyOtpFragment_to_notesFragment)
                    }else{
                        Toast.makeText(requireContext(),"OTP is invalid..", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun hideKeyBoard(){
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }
}