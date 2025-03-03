/* */


// (1) index.html mostrar productos de bd y agregarlos al array de objetos en carrito en localstorage
    async function cargarProductos() {
        // 1. Recibe la respuesta del backend en json
        const res = await fetch('/api/productos');
        // 2. convierte la respuesta json a un array de objetos(productos) (backend lista de productos)
        const productos = await res.json();
        const contenedor = document.getElementById('productos');

        // Array con rutas de imágenes en static/img/
        const imagenes = ["img/fame.JPEG", "img/xsblack.JPEG", "img/lady.JPEG", "img/xs.JPEG"];

        // recorre el array de productos(por indice) para mostrarlos en la pagina index.html en las cards del contenedor
        productos.forEach((p, index) => {
            // una card para cada div
            const card = document.createElement('div');
            card.className = 'd-flex';

            /* Selecciona una imagen del array (cíclico si hay mas productos que imagenes, se repite la imagen),
               al estar dentro de el forEach de productos, se seleciona la imagen segun el indice del producto */
            const imagenSrc = imagenes[index % imagenes.length];

            /* Se crea una instancia de NumberFormat(clase de javascript) para formatear el precio a miles,
               le paso a NumberFormat el precio(bd) se guarda en variable precioFormateado y que se muestre en la card */
            let precioFormateado = new Intl.NumberFormat('es-ES').format(p.precio);
            /* 3. En el boton agregaralCarrito convierte el array a json y lo pasa a la funcion agregar, aunque al pasarlo a onclick,
                  Javascript lo interpreta como objeto, asi que lo envia como objeto a la funcion agregar */
            card.innerHTML = `
                <div class="card mb-4">
                    <img src="${imagenSrc}" class="card-img-top">
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title">${p.nombre}</h5>
                        <p class="card-text">${p.descripcion}</p>
                        <p class="fw-bold">Precio $${precioFormateado}</p>
                        <div class="d-grid gap-2">
                            <button class="btn btn-primary" onclick='agregar(${JSON.stringify(p)})'>Agregar al Carrito</button>
                            <button class="btn btn-success" onclick='irAlCarrito()'>Ir al Carrito</button>
                        </div>
                    </div>
                </div>
            `;
            contenedor.appendChild(card);
        });
    }
    // BOTONES agregaralCarrito e iralCarrito
    // 4. recibe el producto en formato objeto de javascript
    function agregar(producto) {
      // 5. de json a array de objetos(productos) si lo hay, sino lo inicializa vacio [] y se pushea objeto(s)
      let carrito = JSON.parse(localStorage.getItem('carrito')) || [];
      carrito.push(producto);
      localStorage.setItem('carrito', JSON.stringify(carrito));
      alert('Producto agregado');
    }
    // verifica si el usuario esta logueado antes de ir a carrito.hmtl
    function irAlCarrito() {
        let usuario = localStorage.getItem('usuario');
        if (usuario) {
            window.location.href = 'carrito.html';
        } else {
            alert('Para comprar debe iniciar sesión');
            window.location.href = 'iniciarsesion.html';
        }
    }
    cargarProductos();  // los productos se carguen automaticamente cuando la pagina index.html se abre



// (2) carrito.html mostrar y quitar productos agregados a el carrito en localstorage
    function mostrarCarrito() {
        let carrito = JSON.parse(localStorage.getItem('carrito')) || [];
        /* muestra en div de carrito.html los item(productosAgregados) en este caso nombre, precio(bd), total, y,
        boton quitarDelCarrito, estos se guardan en constante lista y se muestre */
        const lista = document.getElementById('listaCarrito');
        lista.innerHTML = '';
        let total = 0;

        // recorre el array de productos(por indice) del carrito
        carrito.forEach((p, index) => {
            const item = document.createElement('div');
            item.className = "d-flex align-items-center justify-content-between border p-2 mb-2";
            // Se pasa a NumberFormat(clase de javascript) el precio(producto agregado) y lo formatea a miles
            let precioFormateado = new Intl.NumberFormat('es-ES').format(p.precio);
            item.innerHTML = `
                <p class="m-0">${p.nombre} - $${precioFormateado}</p>
                <button class="btn btn-danger btn-sm" onclick="quitarDelCarrito(${index})">❌ Quitar del Carrito</button>
            `;

            lista.appendChild(item);
            total += p.precio;  // Operacion de Suma del precio de cada producto agregado, al total inicializado en 0
        });

        // Se pasa a NumberFormat(clase de javascript) el preciototal(suma de todos los precios agregados) y lo formatea a miles
        let totalFormateado = new Intl.NumberFormat('es-ES').format(total);
        document.getElementById('total').innerText = 'Precio Total a Pagar: $' + totalFormateado;
    }
    //BOTON quitarDelCarrito
    function quitarDelCarrito(index) {
        let carrito = JSON.parse(localStorage.getItem('carrito')) || [];
        // splice elimina 1 elemento en la posicion index(indice) del array carrito
        carrito.splice(index, 1);
        localStorage.setItem('carrito', JSON.stringify(carrito));
        // Muestra el carrito actualizado
        mostrarCarrito();
    }
    // muestra los productos agregados
    mostrarCarrito();



    // (3) carrito.html BOTON PAGAR
    async function pagar() {
      let carrito = JSON.parse(localStorage.getItem('carrito')) || [];
      if (carrito.length === 0) {
        alert('Carrito vacío');
        return;
      }
      /* Envía al backend en json el (carrito(productos agregados) y usuario(logueado)) a carritocontroller para crear la factura PDF,
         y si sale bien se espera aqui una respuesta si es ok imprime el pdf en el pc y vacia el carrito sino error */
      const res = await fetch('/api/carrito/factura', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        // Obtiene el usuario(logueado) de localStorage, desde la variable global en usuarios.js(ya que en el html se carga primero)
        body: JSON.stringify({ usuario, carrito })
      });
      if (res.ok) {
        const blob = await res.blob(); // Convierte la respuesta a un blob (archivo binario)
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'factura.pdf';
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        localStorage.removeItem('carrito');
        mostrarCarrito(); // Muestra el carrito vacío
        alert('Pago efectuado con éxito, tu Factura de Compra se descargo en pdf ✅✅    ✅✅   ✅✅   ✅✅', 'success');
      } else {
        alert('Error al generar factura');
      }
    }
    mostrarCarrito(); // Muestra el carrito actualizado