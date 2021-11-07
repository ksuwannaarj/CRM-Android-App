package com.example.rbdb.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.example.rbdb.R
import com.example.rbdb.database.model.CardEntity
import com.example.rbdb.databinding.ViewHolderContactBinding
import com.example.rbdb.ui.ContactFragment

class EditGroupContactsAdapter(
    private val data: MutableList<CardEntity>,
    private var selectedContacts: MutableList<Long>,
    private val contactCardInterface: ContactCardInterface
) : RecyclerView.Adapter<EditGroupContactsAdapter.ContactViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding =
            ViewHolderContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.onBind(data[position], selectedContacts, contactCardInterface, position)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun swapData(data: List<CardEntity>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun swapSelectedContacts(newSelectedContacts: MutableList<Long>) {
        selectedContacts = newSelectedContacts
        notifyDataSetChanged()
    }

    inner class ContactViewHolder(private val binding: ViewHolderContactBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {

        fun onBind(
            contactData: CardEntity,
            selectedContacts: MutableList<Long>,
            contactCardInterface: ContactCardInterface,
            position: Int
        ) {
            val generator: ColorGenerator = ColorGenerator.MATERIAL
            val firstInitial = contactData.name[0].toString() // used for avatar letter
            val whiteSpaceIndex = contactData.name.indexOf(" ")
            val secondInitial =
                contactData.name[whiteSpaceIndex + 1].toString() // used for avatar colour
            val drawable: TextDrawable =
                TextDrawable.builder().buildRound(firstInitial, generator.getColor(secondInitial))



            // Conditional formatting of avatar/green tick if selected
            if (contactData.cardId in selectedContacts) {
                binding.contactAvatar.setImageResource(R.drawable.ic_baseline_check_24)
            } else {
                binding.contactAvatar.setImageDrawable(drawable)
            }

            binding.contactName.text = contactData.name
            binding.contactCompany.text = contactData.business
            binding.contactPhone.text = contactData.phone
            binding.letterDivider.text = firstInitial

            // Check if this is the first contact with a new first letter

            if (position == 0) {
                binding.letterDivider.visibility = View.VISIBLE
            } else {
                val previousContactInitial = data[position - 1].name[0].toString()
                if (firstInitial != previousContactInitial) {
                    binding.letterDivider.visibility = View.VISIBLE
                } else {
                    binding.letterDivider.visibility = View.INVISIBLE
                }
            }

            binding.contactCard.setOnClickListener {
                contactCardInterface.onContactCardClick(absoluteAdapterPosition)
            }
        }
    }
}