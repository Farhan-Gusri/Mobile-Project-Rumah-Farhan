package com.example

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.example.fgf_lays.AuthActivity
import com.example.fgf_lays.databinding.FragmentProfileBinding
import java.io.File
import java.io.FileOutputStream

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // Memilih foto dari galeri dan menyalinnya ke penyimpanan internal aplikasi
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val savedPath = copyUriToInternalStorage(
                requireContext(),
                it,
                "profile_picture.jpg"
            )

            if (savedPath != null) {
                val sharedPref = requireActivity().getSharedPreferences(
                    "user_pref",
                    Context.MODE_PRIVATE
                )

                // Simpan path file lokal (bukan URI sementara galeri)
                sharedPref.edit {
                    putString("profile_image_uri", savedPath)
                }

                // Tampilkan foto dari file lokal
                binding.ivProfilePicture.setImageURI(null) // Reset cache ImageView
                binding.ivProfilePicture.setImageURI(Uri.fromFile(File(savedPath)))

                Toast.makeText(
                    requireContext(),
                    "Foto profil berhasil diperbarui!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Gagal memuat gambar",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences(
            "user_pref",
            Context.MODE_PRIVATE
        )

        // Memuat nama profil
        val savedName = sharedPref.getString("user_name", "Belum diatur")
        binding.tvProfileName.text = "Nama: $savedName"

        // Memuat foto profil yang tersimpan dengan aman
        val savedImagePath = sharedPref.getString("profile_image_uri", null)

        if (!savedImagePath.isNullOrEmpty()) {
            // Cek apakah data lama masih berupa URI "content://" yang menyebabkan crash
            if (savedImagePath.startsWith("content://")) {
                // Hapus URI lama yang kadaluarsa agar tidak crash
                sharedPref.edit { remove("profile_image_uri") }
            } else {
                val file = File(savedImagePath)
                if (file.exists()) {
                    binding.ivProfilePicture.setImageURI(Uri.fromFile(file))
                }
            }
        }

        // Action: Pilih foto profil
        binding.btnChangePhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // Action: Edit nama profil
        binding.btnEditProfile.setOnClickListener {
            showEditProfileDialog(sharedPref)
        }

        // Action: Logout
        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin keluar?")
                .setPositiveButton("Ya") { dialog, _ ->

                    // Hapus data sesi, tetapi pertahankan foto dan nama profil
                    val savedImage = sharedPref.getString("profile_image_uri", null)
                    val savedName = sharedPref.getString("user_name", null)

                    sharedPref.edit {
                        clear()

                        if (savedImage != null) {
                            putString("profile_image_uri", savedImage)
                        }

                        if (savedName != null) {
                            putString("user_name", savedName)
                        }
                    }

                    dialog.dismiss()

                    val intent = Intent(
                        requireActivity(),
                        AuthActivity::class.java
                    )

                    startActivity(intent)
                    requireActivity().finish()
                }
                .setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    // Fungsi menyalin file gambar dari galeri ke folder internal aplikasi
    private fun copyUriToInternalStorage(
        context: Context,
        uri: Uri,
        fileName: String
    ): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val file = File(context.filesDir, fileName)
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Dialog untuk mengubah nama profil
    private fun showEditProfileDialog(
        sharedPref: android.content.SharedPreferences
    ) {
        val input = EditText(requireContext()).apply {
            hint = "Masukkan Nama Baru"
            setText(sharedPref.getString("user_name", ""))
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Nama Profil")
            .setView(input)
            .setPositiveButton("Simpan") { dialog, _ ->

                val newName = input.text.toString().trim()

                if (newName.isNotEmpty()) {
                    sharedPref.edit {
                        putString("user_name", newName)
                    }

                    binding.tvProfileName.text = "Nama: $newName"

                    Toast.makeText(
                        requireContext(),
                        "Profil diperbarui!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}