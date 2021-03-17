let MenuItems = document.getElementById("MenuItems");

MenuItems.style.maxHeight = "0px";
//function menutoggle(){
//    if(MenuItems.style.maxHeight == "0px") {
//        MenuItems.style.maxHeight = "200px";
//    } else {
//        MenuItems.style.maxHeight = "0px";
//    }
//}

let ImgBtn = document.getElementById("ImgBtn");

ImgBtn.addEventListener("click",()=> {
	if(MenuItems.style.maxHeight == "0px") {
        MenuItems.style.maxHeight = "200px";
    } else {
        MenuItems.style.maxHeight = "0px";
    }
	
	
});

document.querySelector('#ImgBtn').addEventListener('click', function() {
  document.querySelector('.section.collapsible').classList.toggle('collapsed');
});


// -----js for product gallery


// function updateCartTotal(){
//     var cartItemContainer = document.getElementsByClassName('cart-info')[0];
//     var cartRows = cartItemContainer.getElementsByTagName('small');
//     var total = 0;
//     for (var i = 0; i < cartRows.length; i++) {
//         var cartRow = cartRows[i];
//         var priceElement = cartRow.getElementsByTagName('small')[0];
//         var quantityElement = cartRow.getElementsByTagName('input')[0];
//         var price = parseInt(priceElement.innerText.replace('$', ''));
//         var quantity = quantityElement.value;
//         total = total + (price * quantity);
//     }

//     document.getElementsByClassName('total-price')[0].innerText = total;

// }


function goBack() {
	window.history.back();
}
      


    