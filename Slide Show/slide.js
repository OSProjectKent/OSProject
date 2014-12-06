var pic = [ "pancake.jpg", "desert.jpg",
            "meat.jpg", "cake.jpg", "pasta.jpg" ];

var slide = new Array();
var index = 0;        // current slide index

function loadImage(url)
{  if (document.images)
   {    rslt = new Image();
        rslt.src = url;
        return rslt;
   }
}

if ( document.images ) 
for(var i in pic) { slide.push(loadImage(pic[i])); }

function prevSlide()
{  if(--index < 0)
   { index = pic.length-1;
   }
   changeSlide();
}

function nextSlide()
{  if( ++index >= pic.length)
   { index = 0
   }
   changeSlide();
}

function changeSlide()
{ 
   document.getElementById('display').src = slide[index].src;
}
