document.menu = new Object();
document.menu.linkboxes = new Array();
var numFolders = 250;		// maximum number of folders to create in array
var openImg = 'menu_images/open.gif';
var closeImg = 'menu_images/closed.gif';

// initialize expanding divs
function initializeMenu(){
	for (var i=0; i<numFolders; i++) {
		var elt = document.getElementById('link'+i);
		document.menu.linkboxes[i] = elt;
		if (elt != null) {
			elt.style.display = 'none'; // make invisible
		}
	}
}

// open/close a specific folder
function changeMenuItem(item){

	// make sure we really have a menu
	if(document.menu.linkboxes.length){
		var folder = document.menu.linkboxes[item];

		if (folder.style.display == 'none'){			// if it's closed, open it
			folder.style.display = '';
			document.images['Img'+item].src = openImg;
		}
		else {
			folder.style.display = 'none';				// if it's open, close it
			document.images['Img'+item].src = closeImg;
		}
	}
}

// show/hide instructions to the user
function imageSwap(img, imgSrc){
  // Check to make sure that images are supported in the DOM.
  // and that we have a menu
  if(document.images && document.menu.linkboxes.length){
    // Check to see whether you are using a name, number, or object
    if (typeof(img) == 'string' && imgSrc) {
      var obj = eval( 'document.'+img);
	  if (obj) {
		obj.src = imgSrc;
	  }
    } else if ((typeof(img) == 'object') && img && imgSrc) {
      img.src = imgSrc;
    }
  }
}

