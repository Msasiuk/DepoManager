const API_BASE_URL = 'http://localhost:8080/api';

// Inicializar productos y tipos al cargar la página
document.addEventListener('DOMContentLoaded', () => {
  fetchProductos();
  fetchTiposProducto();
});

// Listar productos
async function fetchProductos() {
  try {
    const response = await fetch(`${API_BASE_URL}/productos`);
    const productos = await response.json();
    renderProductos(productos);
  } catch (error) {
    console.error('Error al cargar productos:', error);
  }
}

// Cargar tipos de productos y llenar el <select>
async function fetchTiposProducto() {
  try {
    const response = await fetch(`${API_BASE_URL}/tipo-productos`);
    if (!response.ok) throw new Error('Error al cargar tipos de productos');
    
    const tipos = await response.json();
    console.log('Tipos de productos recibidos:', tipos); // <-- Verifica la respuesta aquí

    if (!Array.isArray(tipos)) {
      throw new Error('El formato de los tipos de productos es incorrecto');
    }

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

// Manejar el formulario de creación de productos
document.getElementById('producto-form').addEventListener('submit', async (event) => {
  event.preventDefault();

  const codigo = document.getElementById('producto-codigo').value;

  // Validar si el código ya existe
  if (await codigoExiste(codigo)) {
    alert('El código del producto ya existe.');
    return;
  }

  const nuevoProducto = {
    codigo,
    descripcion: document.getElementById('producto-descripcion').value,
    fechaInicio: document.getElementById('producto-fecha-inicio').value || new Date().toISOString().split('T')[0],
    fechaFin: document.getElementById('producto-fecha-fin').value || '9999-12-31',
    tipoProducto: { id: document.getElementById('producto-tipo').value },  // Enviar objeto con ID
    cantidad: parseInt(document.getElementById('producto-cantidad').value) || 0,
    stockMaximo: parseInt(document.getElementById('producto-stock-max').value) || 0,
    stockMinimo: parseInt(document.getElementById('producto-stock-min').value) || 0,
  };

  try {
    const response = await fetch(`${API_BASE_URL}/productos`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(nuevoProducto),
    });

    if (response.ok) {
      alert('Producto ingresado exitosamente');
      fetchProductos();
      event.target.reset();
    } else {
      alert('Error al ingresar el producto. Verifique el código.');
    }
  } catch (error) {
    console.error('Error al ingresar producto:', error);
  }
});

// Verificar si un código ya existe
async function codigoExiste(codigo) {
  try {
    const response = await fetch(`${API_BASE_URL}/productos`);
    const productos = await response.json();
    return productos.some(producto => producto.codigo === codigo);
  } catch (error) {
    console.error('Error al verificar el código:', error);
    return false;
  }
}

// Renderizar productos en la interfaz
function renderProductos(productos) {
  const list = document.getElementById('producto-list');
  list.innerHTML = ''; // Limpiar la lista

  productos.forEach(producto => {
    const item = document.createElement('div');
    item.className = 'list-item';

    item.innerHTML = `
      <div>
        <strong>${producto.codigo}</strong> - ${producto.descripcion} <br>
        <small>Fecha Inicio: ${producto.fechaInicio} | Fecha Fin: ${producto.fechaFin}</small> <br>
        <small>Tipo: ${producto.tipoProducto.descripcion} | Cantidad: ${producto.cantidad}</small> <br>
        <small>Stock Máximo: ${producto.stockMaximo} | Stock Mínimo: ${producto.stockMinimo}</small>
      </div>
      <button onclick="deleteProducto(${producto.id})">Eliminar</button>
    `;
    list.appendChild(item);
  });
}

// Eliminar producto
async function deleteProducto(id) {
  if (confirm('¿Está seguro de que desea eliminar este producto?')) {
    try {
      const response = await fetch(`${API_BASE_URL}/productos/${id}`, { method: 'DELETE' });
      if (response.ok) {
        alert('Producto eliminado exitosamente');
        fetchProductos();
      } else {
        alert('Error al eliminar el producto.');
      }
    } catch (error) {
      console.error('Error al eliminar producto:', error);
    }
  }
}