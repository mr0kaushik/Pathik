package com.pathik.ride.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pathik.ride.R
import com.pathik.ride.databinding.WalletItemBinding
import com.pathik.ride.model.Upi
import com.pathik.ride.model.UpiType
import kotlin.properties.Delegates


class UpiAdapter(
    private val context: Context,
    private val onUpiClick: (upi: Upi) -> Unit
) :
    RecyclerView.Adapter<UpiAdapter.UpiViewHolder>() {

    private var upis: List<Upi> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpiViewHolder {
        return UpiViewHolder(
            WalletItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UpiViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val upi = upis[position]
            holder.binding.tvWalletTitle.text = upi.name
            holder.binding.ivWalletLogo.setImageResource(getWalletImageSource(upi.upiType))

            holder.itemView.setOnClickListener {
                onUpiClick.invoke(upi)
            }
        }
    }


    private fun getWalletImageSource(upiType: UpiType): Int {
        return when (upiType) {
            UpiType.BHIM_UPI -> R.drawable.upi
            UpiType.PAY_TM -> R.drawable.paytm
            UpiType.PHONE_PE -> R.drawable.phone_pe
            UpiType.GOOGLE_PAY -> R.drawable.gpay
            UpiType.AMAZON_PAY -> R.drawable.amazonpay
        }
    }

    override fun getItemCount(): Int = upis.size

    public fun updateWalletList(list: List<Upi>) {
        upis = list
    }

    class UpiViewHolder(val binding: WalletItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}