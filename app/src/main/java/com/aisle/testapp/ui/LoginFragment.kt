package com.aisle.testapp.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aisle.testapp.R
import com.aisle.testapp.databinding.FragmentLoginBinding
import com.aisle.testapp.others.AppConstants
import com.aisle.testapp.others.NetworkResult
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment(),View.OnClickListener {

    private lateinit var binding: FragmentLoginBinding

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnContinue.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if(p0?.id==R.id.btnContinue){
            loginUser()
        }
    }

    private fun loginUser() {
        val countryCode = binding.etCountryCode.text.toString().trim()
        val mobNum = binding.etMobNum.text.toString().trim()
        if(mobNum.isEmpty()){
            Toast.makeText(requireContext(),"Please enter a phone number",Toast.LENGTH_LONG).show()
            return
        }
        val jsonObject = JsonObject()
        jsonObject.addProperty("number", countryCode.plus(mobNum))
        loginViewModel.login(jsonObject)

        loginViewModel.loginResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Failure -> {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(requireContext(),it.errorMessage,Toast.LENGTH_LONG).show()
                }
                is NetworkResult.Loading -> {
                    hideKeyBoard()
                    binding.progress.visibility = View.VISIBLE
                }
                is NetworkResult.Success -> {
                    binding.progress.visibility = View.GONE
                    val result = it.data as JsonObject
                    if(result.get("status").asBoolean){
                        val bundle  = Bundle()
                        bundle.putString(AppConstants.COUNTRY_CODE,binding.etCountryCode.text.toString())
                        bundle.putString(AppConstants.MOBILE_NUM,binding.etMobNum.text.toString())
                        findNavController().navigate(R.id.action_loginFragment_to_verifyOtpFragment,bundle)
                    }else{
                        Toast.makeText(requireContext(),"Phone Number is invalid..",Toast.LENGTH_LONG).show()
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