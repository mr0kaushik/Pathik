package com.pathik.ride.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pathik.ride.R
import com.pathik.ride.databinding.WalletItemBinding
import com.pathik.ride.model.Wallet
import com.pathik.ride.model.WalletType
import com.pathik.ride.utils.visible
import kotlin.properties.Delegates


class WalletAdapter(
    private val context: Context,
    private val onWalletClicked: (wallet: Wallet) -> Unit
) :
    RecyclerView.Adapter<WalletAdapter.WalletViewHolder>() {

    private var wallets: List<Wallet> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        return WalletViewHolder(
            WalletItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val wallet = wallets[position]
            holder.binding.tvWalletTitle.text = wallet.name
            holder.binding.tvAction.text =
                context.getString(if (wallet.hasLinked) R.string.check_balance else R.string.link)
            holder.binding.tvAction.visible(true)
            holder.binding.ivWalletLogo.setImageResource(getWalletImageSource(wallet.walletType))

            holder.itemView.setOnClickListener {
                onWalletClicked.invoke(wallet)
            }
        }
    }


    private fun getWalletImageSource(walletType: WalletType): Int {
        return when (walletType) {
            WalletType.PAY_TM -> R.drawable.paytm
            WalletType.PHONE_PE -> R.drawable.phone_pe
            WalletType.AMAZON_PAY -> R.drawable.amazonpay
        }
    }

    override fun getItemCount(): Int = wallets.size

    public fun updateWalletList(list: List<Wallet>) {
        wallets = list
    }

    class WalletViewHolder(val binding: WalletItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}