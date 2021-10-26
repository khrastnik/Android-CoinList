package com.coinlist

import android.os.Bundle
import com.coinlist.databinding.CoinListActivityBinding
import com.coinlist.ui.CoinListFragment
import com.coinlist.ui.base.BaseActivity

class CoinListActivity : BaseActivity() {

    private lateinit var binding: CoinListActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CoinListActivityBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, CoinListFragment.newInstance())
                .commitNow()
        }
    }
}
