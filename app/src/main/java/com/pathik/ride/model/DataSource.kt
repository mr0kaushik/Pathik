package com.pathik.ride.model

import com.google.firebase.Timestamp

object DataSource {

    val listOfWallet = listOf(
        Wallet(
            id = "wallet1",
            name = "PayTM",
            hasLinked = true,
            token = "paytm_wallet_token",
            walletType = WalletType.PAY_TM
        ),
        Wallet(
            id = "wallet2",
            name = "Phone Pe",
            hasLinked = false,
            token = "phone_pe_wallet_token",
            walletType = WalletType.PHONE_PE
        ),
        Wallet(
            id = "wallet1",
            name = "Amazon Pay",
            hasLinked = true,
            token = "paytm_wallet_token",
            walletType = WalletType.AMAZON_PAY
        ),
    )

    val listOfUpi = listOf(
        Upi(
            id = "upi",
            name = "Google Pay",
            hasLinked = true,
            token = "paytm_wallet_token",
            upiType = UpiType.GOOGLE_PAY
        ),
        Upi(
            id = "wallet2",
            name = "BHIM UPI",
            hasLinked = false,
            token = "phone_pe_wallet_token",
            upiType = UpiType.BHIM_UPI
        ),
        Upi(
            id = "wallet1",
            name = "Pay TM UPI",
            hasLinked = true,
            token = "paytm_wallet_token",
            upiType = UpiType.PAY_TM
        ),
    )


    val listOfTrips = listOf(
        Trip(
            id = "trip1",
            vehicleInfo = "Cab: DL 1912",
            date = Timestamp.now(),
            source = "Dwarka Sector 3, New Delhi",
            destination = "Investopad, Gurugram, Haryana",
            amount = 230.0,
        ),
        Trip(
            id = "trip2",
            vehicleInfo = "Cab: DL 2312",
            date = Timestamp.now(),
            source = "Dwarka Sector 16, New Delhi",
            destination = "Ambience Mall, Gurugram, Haryana",
            amount = 200.0,
        ),
        Trip(
            id = "trip3",
            vehicleInfo = "Cab: DL 2901",
            date = Timestamp.now(),
            source = "India Gate, New Delhi",
            destination = "Red fort, New Delhi",
            amount = 100.0,
        ),
        Trip(
            id = "trip4",
            vehicleInfo = "Cab: DL 1912",
            date = Timestamp.now(),
            source = "Dwarka mor metro station",
            destination = "Lajpat Nagar, New Delhi",
            amount = 290.0,
        ),
        Trip(
            id = "trip5",
            vehicleInfo = "Auto: DL 9021",
            date = Timestamp.now(),
            source = "Vasant Gaon, Delhi",
            destination = "Nehru Place Metro Station, Delhi",
            amount = 180.0,
        ),
        Trip(
            id = "trip6",
            vehicleInfo = "Bike: DL 3131",
            date = Timestamp.now(),
            source = "Dwarka Sector 3, New Delhi",
            destination = "Dwarka Sector 16, New Delhi",
            amount = 45.0,
        ),
    )


}