package com.mylearninghub.favdish.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mylearninghub.favdish.R
import com.mylearninghub.favdish.databinding.FragmentAllDishesBinding
import com.mylearninghub.favdish.view.activity.AddUpdateDishActivity
import com.mylearninghub.favdish.viewmodel.AllDishesFragment

class AllDishesFragment: Fragment() {

    private var _binding: FragmentAllDishesBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(AllDishesFragment::class.java)

        _binding = FragmentAllDishesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val menuHost:MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater)
            {
                menuInflater.inflate(R.menu.add_dish_menu, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean
            {
                when(menuItem.itemId)
                {
                    R.id.action_add_dish -> {
                        startActivity(Intent(requireActivity(), AddUpdateDishActivity::class.java))
                        return true
                    }
                }
                return true
            }
        })
    }


}