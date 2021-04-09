package com.pathik.ride.ui.activities.payment

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.pathik.ride.R
import com.pathik.ride.databinding.ActivityPaymentBinding
import com.pathik.ride.databinding.AppBarLayoutBinding
import com.pathik.ride.model.Upi
import com.pathik.ride.model.Wallet
import com.pathik.ride.network.Resource
import com.pathik.ride.ui.adapter.UpiAdapter
import com.pathik.ride.ui.adapter.WalletAdapter
import com.pathik.ride.utils.snackbar
import com.pathik.ride.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity() {

    private val viewModel by viewModels<PaymentViewModel>()
    private var wallets: List<Wallet> = emptyList()
    private var upis: List<Upi> = emptyList()

    private lateinit var walletAdapter: WalletAdapter
    private lateinit var upiAdapter: UpiAdapter
    private lateinit var binding: ActivityPaymentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setViewVisible(false)

        val appBarLayoutBinding = AppBarLayoutBinding.bind(binding.root.getChildAt(0))
        appBarLayoutBinding.tvAppBarTitle.text = getString(R.string.payment)
        appBarLayoutBinding.btnBack.setOnClickListener {
            onBackPressed()
        }

        walletAdapter = WalletAdapter(this) {
            binding.root.snackbar("Linking of ${it.name} is in progress!!")
        }

        upiAdapter = UpiAdapter(this) {
            binding.root.snackbar("Opening UPI App ${it.name} is in progress!!")
        }


        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshList(true)
        }
        binding.rvWallets.apply {
            layoutManager = LinearLayoutManager(this@PaymentActivity)
            adapter = walletAdapter
        }
        binding.rvUPI.apply {
            layoutManager = LinearLayoutManager(this@PaymentActivity)
            adapter = upiAdapter
        }

        refreshList(false)
    }

    private fun refreshList(fromSwipe: Boolean) {
        viewModel.getLocalPayments().observe(this, {
            when (it) {
                is Resource.Loading -> {
                    if (!fromSwipe) binding.swipeRefreshLayout.isRefreshing = true
                }
                is Resource.Success -> {
                    upis = it.value.first
                    wallets = it.value.second
                    walletAdapter.updateWalletList(wallets)
                    upiAdapter.updateWalletList(upis)
                    binding.swipeRefreshLayout.isRefreshing = false

                    setViewVisible(true)
                }
                is Resource.Failure -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.root.snackbar(
                        it.exception?.message!!,
                        Snackbar.LENGTH_INDEFINITE,
                        getString(R.string.refresh)
                    ) {
                        binding.swipeRefreshLayout.isRefreshing = true
                    }
                }
            }
        })
    }

    private fun setViewVisible(isVisible: Boolean) {
        binding.tvWallet.visible(isVisible)
        binding.tvUPI.visible(isVisible)
        binding.tvCard.visible(isVisible)
    }


}