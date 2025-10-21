package com.example.mt5monitor.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mt5monitor.domain.model.AccountOverview

@Composable
fun AccountSummaryCard(account: AccountOverview, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Account Overview", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Balance", fontWeight = FontWeight.Bold)
                    Text("${account.currency} %.2f".format(account.balance))
                }
                Column {
                    Text("Equity", fontWeight = FontWeight.Bold)
                    Text("${account.currency} %.2f".format(account.equity))
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("P/L", fontWeight = FontWeight.Bold)
                    Text(
                        text = "${account.currency} %.2f".format(account.profit),
                        color = if (account.profit >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
