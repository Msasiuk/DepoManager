const API_BASE_URL = 'http://localhost:8080/api';
let productoIdActual = null; // Para saber si estamos creando o modificando un producto

// Inicializar productos y tipos al cargar la página
document.addEventListener('DOMContentLoaded', () => {
  fetchProductos();
  fetchTiposProducto();
  inicializarFormulario();
});

// Inicializar evento del formulario
function inicializarFormulario() {
  const productoForm = document.getElementById('producto-form');
  
  // Registrar el evento 'submit' del formulario
  productoForm.addEventListener('submit', async (event) => {
    event.preventDefault(); // Evitar recarga de página

	// Validación de valores positivos
	    const cantidad = parseInt(document.getElementById('producto-cantidad').value) || 0;
	    const stockMaximo = parseInt(document.getElementById('producto-stock-max').value) || 0;
	    const stockMinimo = parseInt(document.getElementById('producto-stock-min').value) || 0;

	    if (cantidad < 0 || stockMaximo < 0 || stockMinimo < 0) {
	      alert('La cantidad y los stocks deben ser valores positivos.');
	      return;
	    }
	
    const nuevoProducto = {
      codigo: document.getElementById('producto-codigo').value,
      descripcion: document.getElementById('producto-descripcion').value,
      fechaInicio: new Date().toISOString().split('T')[0], // Fecha actual
      fechaFin: document.getElementById('producto-fecha-fin').value || '9999-12-31',
      tipoProducto: { id: document.getElementById('producto-tipo').value },
      cantidad: parseInt(document.getElementById('producto-cantidad').value) || 0,
      stockMaximo: parseInt(document.getElementById('producto-stock-max').value) || 0,
      stockMinimo: parseInt(document.getElementById('producto-stock-min').value) || 0,
    };

    if (productoIdActual) {
      // Modificar un producto existente
      await modificarProducto(productoIdActual, nuevoProducto);
    } else {
      // Crear un producto nuevo
      await crearProducto(nuevoProducto);
    }

    // Resetear el formulario y actualizar la lista de productos
    productoForm.reset();
    productoIdActual = null;
    fetchProductos();
  });
}


// Obtener y renderizar los productos en la interfaz
async function fetchProductos() {
  try {
    const response = await fetch(`${API_BASE_URL}/productos`);
    const productos = await response.json();
    renderProductos(productos);
  } catch (error) {
    console.error('Error al cargar productos:', error);
  }
}

function renderProductos(productos) {
  const list = document.getElementById('producto-list');
  list.innerHTML = '';

  productos.forEach(producto => {
    const item = document.createElement('div');
    item.className = 'list-item';

    item.innerHTML = `
      <div>
        <strong>${producto.codigo}</strong> - ${producto.descripcion} <br>
        <small>Fecha Inicio: ${formatFecha(producto.fechaInicio)} | Fecha Fin: ${formatFecha(producto.fechaFin)}</small> <br>
        <small>Tipo: ${producto.tipoProducto.descripcion} | Cantidad: ${producto.cantidad}</small> <br>
        <small>Stock Máximo: ${producto.stockMaximo} | Stock Mínimo: ${producto.stockMinimo}</small>
      </div>
      <button onclick="deleteProducto(${producto.id})">Eliminar</button>
      <a href="modificar-producto.html?id=${producto.id}"><button>Modificar</button></a>
    `;
    list.appendChild(item);
  });
}

// Formatear fecha como YYYY-MM-DD
function formatFecha(fecha) {
  return new Date(fecha).toISOString().split('T')[0];
}

// Cargar producto en el formulario para modificarlo
async function cargarProductoEnFormulario(id) {
  try {
    const response = await fetch(`${API_BASE_URL}/productos/${id}`);
    const producto = await response.json();

    // Rellenar los campos del formulario
    document.getElementById('producto-codigo').value = producto.codigo;
    document.getElementById('producto-descripcion').value = producto.descripcion;
    document.getElementById('producto-fecha-inicio').value = formatFecha(producto.fechaInicio);
    document.getElementById('producto-fecha-fin').value = formatFecha(producto.fechaFin);
    
    // Validar si `tipoProducto` está presente y asignar su ID al select
    if (producto.tipoProducto && producto.tipoProducto.id) {
      document.getElementById('producto-tipo').value = producto.tipoProducto.id;
    } else {
      console.error('El producto no tiene tipoProducto asignado.');
    }

    document.getElementById('producto-cantidad').value = producto.cantidad;
    document.getElementById('producto-stock-max').value = producto.stockMaximo;
    document.getElementById('producto-stock-min').value = producto.stockMinimo;

    productoIdActual = id; // Guardamos el ID del producto a modificar
  } catch (error) {
    console.error('Error al cargar el producto:', error);
  }
}

// Manejar el formulario para crear o modificar productos
document.getElementById('producto-form').addEventListener('submit', async (event) => {
  event.preventDefault();

  const nuevoProducto = {
    codigo: document.getElementById('producto-codigo').value,
    descripcion: document.getElementById('producto-descripcion').value,
    fechaInicio: document.getElementById('producto-fecha-inicio').value,
    fechaFin: document.getElementById('producto-fecha-fin').value,
    tipoProducto: { id: document.getElementById('producto-tipo').value },
    cantidad: parseInt(document.getElementById('producto-cantidad').value) || 0,
    stockMaximo: parseInt(document.getElementById('producto-stock-max').value) || 0,
    stockMinimo: parseInt(document.getElementById('producto-stock-min').value) || 0,
  };

  if (productoIdActual) {
    // Si hay un ID, estamos modificando un producto existente
    await modificarProducto(productoIdActual, nuevoProducto);
  } else {
    // Si no hay ID, estamos creando un producto nuevo
    await crearProducto(nuevoProducto);
  }

  // Resetear el formulario y actualizar la lista de productos
  event.target.reset();
  productoIdActual = null;
  fetchProductos();
});

// Crear un producto nuevo
async function crearProducto(producto) {
  try {
    const response = await fetch(`${API_BASE_URL}/productos`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(producto),
    });

    if (response.ok) {
      alert('Producto creado exitosamente.');
    } else {
      alert('Codigo ya existente. Error al crear el producto.');
    }
  } catch (error) {
    console.error('Error al crear el producto:', error);
  }
}

// Modificar un producto existente
async function modificarProducto(id, producto) {
  try {
    const response = await fetch(`${API_BASE_URL}/productos/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(producto),
    });

    if (response.ok) {
      alert('Producto modificado exitosamente.');
    } else {
      alert('Error al modificar el producto.');
    }
  } catch (error) {
    console.error('Error al modificar el producto:', error);
  }
}

// Eliminar un producto
async function deleteProducto(id) {
  if (confirm('¿Está seguro de que desea eliminar este producto?')) {
    try {
      const response = await fetch(`${API_BASE_URL}/productos/${id}`, { method: 'DELETE' });
      if (response.ok) {
        alert('Producto eliminado exitosamente.');
        fetchProductos();
      }
    } catch (error) {
      console.error('Error al eliminar producto:', error);
    }
  }
}

// Obtener tipos de producto y llenar el <select>
async function fetchTiposProducto() {
  try {
    const response = await fetch(`${API_BASE_URL}/tipo-productos`);
    const tipos = await response.json();

    renderTiposProducto(tipos);
  } catch (error) {
    console.error('Error al cargar tipos de productos:', error);
  }
}


// Rellenar el <select> con los tipos de productos
function renderTiposProducto(tipos) {
  const tipoSelect = document.getElementById('producto-tipo');
  tipoSelect.innerHTML = ''; // Limpiar el select

  // Añadir la opción por defecto
  const defaultOption = document.createElement('option');
  defaultOption.value = '';
  defaultOption.textContent = 'Seleccione un tipo de producto';
  defaultOption.disabled = true;
  defaultOption.selected = true;
  tipoSelect.appendChild(defaultOption);

  // Añadir las opciones de los tipos de producto
  tipos.forEach(tipo => {
    const option = document.createElement('option');
    option.value = tipo.id;  // Asegúrate de que el valor sea el ID
    option.textContent = tipo.descripcion;  // Mostrar la descripción del tipo
    tipoSelect.appendChild(option);
  });
}
