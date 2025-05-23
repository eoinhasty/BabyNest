import {useEffect, useState} from "react";
import DataTable from "datatables.net-react";
import DT from 'datatables.net-dt';
import '../css/AllProducts.css';

DataTable.use(DT);

function AllProducts() {
    const [products, setProducts] = useState([]);
    const [minPrice, setMinPrice] = useState(0);
    const [maxPrice, setMaxPrice] = useState(Infinity);
    const [categories, setCategories] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState("All");
    const [filteredProducts, setFilteredProducts] = useState([]);

    //Fetch data from the Backend
    const fetchApi = async () => {
        let productsUrl = 'http://localhost:8888/api/products/';
        const productResponse = await fetch(productsUrl);
        let categoriesUrl = 'http://localhost:8888/api/categories/';
        const categoriesResponse = await fetch(categoriesUrl);

        const productData = await productResponse.json();
        const categoriesData = await categoriesResponse.json();

        setProducts(productData);
        setCategories(categoriesData);
    }

    //Call fetchApi when the component is mounted
    useEffect(() => {
        fetchApi();
    }, []);

    //Filter products based on the selected category, minPrice and maxPrice
    useEffect(() => {
        if (products.length > 0) {
            const filtered = products.filter((product) => {
                const price = parseFloat(product.price) || 0;

                const min = parseFloat(minPrice) || 0;
                const max = parseFloat(maxPrice) || Infinity;

                switch (selectedCategory) {
                    case "All":
                        return price >= min && price <= max;
                    default:
                        return product.category &&
                            product.category.name === selectedCategory &&
                            price >= min &&
                            price <= max;
                }
            });
            setFilteredProducts(filtered);
        } else {
            setFilteredProducts([]);
        }
    }, [products, minPrice, maxPrice, selectedCategory]);

    //Format price to EUR
    const formatPrice = (price) => {
        return new Intl.NumberFormat('en-IE', {style: 'currency', currency: 'EUR'}).format(price);
    }

    //Define the columns for the DataTable
    const columns = [
        {data: "name", title: "Name"},
        {data: "description", title: "Description"},
        {
            data: null,
            title: "Price",
            render: data => formatPrice(data.price),
        },
        {data: "category.name", title: "Category"},
        {
            data: null,
            title: "Image",
            render: (data, type, row) =>
                `<a href="/products/${row.productId}">
                    <img
                        src="http://localhost:8888/assets/images/thumbs/${row.productId}/${row.feature_image}"
                        alt="${row.name}"
                        style="width: 100px;"
                    />
                </a>`
        },
    ];

    //Render the component
    return (
        <div className={"products"}>
            <form className="all-products-form">
                <div className="all-products-input-wrapper">
                    <label htmlFor="minPrice">Min Price:</label>
                    <input
                        type="number"
                        id="minPrice"
                        name="minPrice"
                        value={minPrice === 0 ? '0.00' : minPrice}
                        min={0}
                        step={0.5}
                        onChange={(e) => {
                            const value = e.target.value;
                            setMinPrice(value);
                        }}
                        onBlur={(e) => {
                            const value = parseFloat(e.target.value);
                            if (!isNaN(value)) {
                                setMinPrice(value.toFixed(2));
                            } else {
                                setMinPrice('0.00');
                            }
                        }}
                    />
                </div>

                <div className="all-products-input-wrapper">
                    <label htmlFor="maxPrice">Max Price:</label>
                    <input
                        type="number"
                        id="maxPrice"
                        name="maxPrice"
                        value={maxPrice}
                        min={0}
                        step={0.5}
                        onChange={(e) => {
                            const value = e.target.value;
                            setMaxPrice(value);
                        }}
                        onBlur={(e) => {
                            const value = parseFloat(e.target.value);
                            if (!isNaN(value)) {
                                setMaxPrice(value.toFixed(2));
                            } else {
                                setMaxPrice('');
                            }
                        }}
                    />
                </div>

                <div className="all-products-input-wrapper">
                    <label htmlFor="category">Category:</label>
                    <select
                        id="category"
                        name="category"
                        value={selectedCategory}
                        onChange={(categoryInput) => setSelectedCategory(categoryInput.target.value)}
                    >
                        <option value="All">All</option>
                        {categories.map((category) => (
                            <option key={category.categoryId} value={category.name}>
                                {category.name}
                            </option>
                        ))}
                    </select>
                </div>
            </form>

            <DataTable
                id={"myDataTable"}
                data={filteredProducts}
                className={"display"}
                columns={columns}

                options={{
                    paging: true,
                    searching: true,
                    ordering: false,
                    responsive: true,
                    deferRender: true,
                    language: {
                        emptyTable: "No products found",
                        info: "Showing _START_ to _END_ of _TOTAL_ products",
                        lengthMenu: "_MENU_ products per page",
                    },
                    drawCallback: function () {
                        this.api().columns.adjust();
                    },
                }}
            >
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Price</th>
                    <th>Category</th>
                    <th>Image</th>
                </tr>
                </thead>
            </DataTable>
        </div>
    );
}

export default AllProducts;