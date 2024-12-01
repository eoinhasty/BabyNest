import { useEffect, useState } from "react";
import DataTable from "datatables.net-react";
import DT from 'datatables.net-dt';
import "../css/MyOrders.css";
import Cookies from "js-cookie";

DataTable.use(DT);

function MyOrders() {
    const [orders, setOrders] = useState([]);

    //Fetch data from the Backend
    const fetchApi = async () => {
        let ordersUrl = 'http://localhost:8888/api/orders/getOrders';
        const ordersResponse = await fetch(ordersUrl, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${Cookies.get('jwt')}`
            }
        });

        const ordersData = await ordersResponse.json();

        setOrders(ordersData);
    }

    useEffect(() => {
        fetchApi();
    }, []);

    const formatPrice = (price) => {
        return new Intl.NumberFormat("en-IE", { style: "currency", currency: "EUR" }).format(price);
    };

    const formatDate = (date) => {
        return new Date(date).toLocaleDateString("en-IE", {
            year: "numeric",
            month: "long",
            day: "numeric",
        });
    }

    const columns = [
        {
            data: "orderDate",
            title: "Order Date",
            render: (data) => formatDate(data),
        },
        { data: "status", title: "Status" },
        {
            data: "totalAmount",
            title: "Total Amount",
            render: (data) => formatPrice(data),
        },
        {
          data: "estimatedShippingDate",
            title: "Estimated Shipping Date",
            render: (data) => formatDate(data),
        },
        {
            data: null,
            title: "Details",
            render: (data, type, row) =>
                `<a href="/orders/${row.orderId}" class="details-link">View Details</a>`,
        },
    ];

    return (
        <div className="my-orders">
            <h1>My Orders</h1>
            <DataTable
                data={orders}
                columns={columns}
                options={{
                    paging: true,
                    searching: false,
                    ordering: false,
                    language: {
                        emptyTable: "No orders found",
                        info: "Showing _START_ to _END_ of _TOTAL_ orders",
                        infoEmpty: "",
                        lengthMenu: "_MENU_ orders per page",
                    },
                    drawCallback: function () {
                        this.api().columns.adjust();
                    },
                }}
                className="display"
            >
                <thead>
                <tr>
                    <th>Order Date</th>
                    <th>Status</th>
                    <th>Total Amount</th>
                    <th>Estimated Shipping Date</th>
                    <th>Details</th>
                </tr>
                </thead>
            </DataTable>
        </div>
    );
}

export default MyOrders;