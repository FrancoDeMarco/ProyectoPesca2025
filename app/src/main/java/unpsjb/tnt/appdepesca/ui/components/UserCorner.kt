package unpsjb.tnt.appdepesca.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun UserCorner(username: String?){
    if (username!= null){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
        ){
            Text(
                text = "Bienvenido, $username",
                modifier = Modifier.align(Alignment.TopEnd)
                    .padding(top = 10.dp, end = 0.dp),
                color = Color.White
            )
        }
    }
}
