package co.abacus.android.dbconnector.demo.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import co.abacus.android.dbconnector.demo.R
import co.abacus.android.dbconnector.demo.adapter.DisplayItemAdapter
import co.abacus.android.dbconnector.demo.application.AbacusDemoApplication
import co.abacus.android.dbconnector.demo.databinding.ActivityDatabaseDetailBinding
import co.abacus.android.dbconnector.demo.model.DisplayItem
import co.abacus.android.dbconnector.demo.util.stdDispatchers
import co.abacus.dbconnector.*
import co.abacus.dbconnector.data.*
import io.reactivex.disposables.Disposable
import java.math.BigDecimal
import java.util.*

class DatabaseDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDatabaseDetailBinding
    private val itemAdapter = DisplayItemAdapter()

    lateinit var productsRepo: ProductsRepository
    lateinit var categoriesRepo: CategoriesRepository
    lateinit var reportsRepo: ReportsRepository
    lateinit var invoicesRepo: InvoicesRepository

    private var sub: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as AbacusDemoApplication).apply {
            productsRepo = getProductsRepo()
            categoriesRepo = getCategoriesRepo()
            val auth = getAuthService()
            reportsRepo = auth.obtainReportsRepoFor(auth.getCurrUser()!!)
            invoicesRepo = auth.obtainInvoicesRepoFor(auth.getCurrUser()!!)
        }
        binding = ActivityDatabaseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Data Retrieving Service"

        binding.resultsRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                this@DatabaseDetailActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = itemAdapter
        }

        binding.getProducts.setOnClickListener(::onGetProductsClicked)
        binding.getCategories.setOnClickListener(::onGetCategoriesClicked)
        binding.getReports.setOnClickListener(::onGetReportsClicked)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.option_add_category -> {
                // TODO: show dialog box
                val newCat = Category(name = "Fred", description = "Test", isArchived = false)
                categoriesRepo.add(newCat)
                    .stdDispatchers()
                    .subscribe(
                        {
                            Toast.makeText(this, "Success, ID=${it.categoryID}", Toast.LENGTH_SHORT).show()
                        },
                        { err -> showError(err) }
                    )
                true
            }
            R.id.option_add_invoice -> {
                val total = BigDecimal.valueOf(Random().nextInt(400).toDouble())
                val invoice = makeInvoice(total, BigDecimal.valueOf(1.0))

                invoicesRepo.add(invoice)
                    .stdDispatchers()
                    .subscribe(
                        { newInvoice ->
                            val invoiceId = newInvoice.id  // Id should be auto-generated
                            if (invoiceId == null) {
                                showError("invoiceId was not generated")
                            } else {
                                val items = listOf(
                                    makeItem(invoiceId, total.divide(BigDecimal.valueOf(2))),
                                    makeItem(invoiceId, total.divide(BigDecimal.valueOf(2)))
                                )
                                val s =  invoicesRepo.addItems(items)
                                    .stdDispatchers()
                                    .subscribe(
                                        {
                                            Toast.makeText(this, "SUCCESS!", Toast.LENGTH_SHORT).show()
                                        },
                                        { err -> showError(err) }
                                    )
                            }

                        },
                        { err -> showError(err) }
                    )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun makeInvoice(total: BigDecimal, tax: BigDecimal): Invoice =
        Invoice(
            createdOn = Date(),
            uniqueCode = UUID.randomUUID(),
            createdByName = "Android Test",
            companyId = "",  // THIS will get assigned byt the .add() operation
            internalId = "0",
            invoiceType = Invoice.InvoiceType.DELIVERY,
            paymentMethod = "Cash",
            paymentMethodId = "0",
            paymentTypeName = "C",
            paymentOn = Date(),
            platform = "0",
            currency = "AUD",
            taxRate = 0.0,
            totalExTax = total.minus(tax),
            taxAmount = tax,
            total = total,
            shippingAddress = PostalAddress(
                "3 Main St",
                "Mainsville",
                "3011",
                "VIC",
                "Australia"
            )
        )

    private fun makeItem(invoiceId: String, amount: BigDecimal): InvoiceItem =
        InvoiceItem(
            invoiceId = invoiceId,
            totalExTax = amount,
            tax = BigDecimal.valueOf(0.0),
            createdOn = Date(),
            itemId = "2",
            itemName = "Item2",
            productCode = "DEF",
            categoryName = "Cat1",
            quantity = 1.0,
            unitPrice = amount,
            total = amount
        )

    private fun onGetProductsClicked(view: View) {
        sub?.dispose()
        sub = productsRepo.getAll()
            .stdDispatchers()
            .doFinally { sub = null }
            .subscribe(
                { products ->
                    itemAdapter.setItems(products.map {
                        DisplayItem(
                            id = it.productId,
                            name = it.name
                        )
                    })
                },
                { err -> showError(err) }
            )
    }

    private fun onGetCategoriesClicked(view: View) {
        sub?.dispose()
        sub = categoriesRepo.getAll()
            .stdDispatchers()
            .doFinally { sub = null }
            .subscribe(
                { categories ->
                    itemAdapter.setItems(categories.map {
                        DisplayItem(
                            id = if (it.categoryID.isNotBlank()) it.categoryID else "null value",
                            name = if (it.name != null) it.name else "null value"
                        )
                    })
                },
                { err -> showError(err) }
            )
    }

    private fun onGetReportsClicked(view: View) {
        sub?.dispose()
        val today = Date()
        sub = reportsRepo.invoiceItemSummaryByItemAndCategoryFor(today, today)
            .stdDispatchers()
            .doFinally { sub = null }
            .subscribe(
                { items ->
                    itemAdapter.setItems(items.map {
                        DisplayItem(
                            id = it.groupByName,
                            name = "${it.total}"
                        )
                    })
                },
                { err -> showError(err) }
            )
    }

    private fun showError(err: Throwable) {
        Toast.makeText(this, err.message ?: "Error", Toast.LENGTH_SHORT).show()
    }

    private fun showError(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        sub?.dispose()
        sub = null
        super.onStop()
    }
}