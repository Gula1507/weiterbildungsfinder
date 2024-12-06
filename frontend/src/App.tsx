import './App.css'
import {Route, Routes} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import {Organization} from "./types/Organization.ts";
import Home from "./components/Home.tsx";
import OrganizationForm from "./components/OrganizationForm.tsx";
import OrganizationDetails from "./components/OrganizationDetails.tsx";
import Header from "./components/Header.tsx";


function App() {
    const [organizations, setOrganizations] = useState<Organization[]>([])
    const [loading, setLoading] = useState<boolean>(true);

    const loadOrganizations = () => {
        setLoading(true);
        axios.get("/api/organizations").then((response) => {
            setOrganizations(response.data);
            setLoading(false);
            }
        )
            .catch((error) => {
                console.error(error);
                setLoading(false);
            })
    }
    useEffect(() => {
        loadOrganizations()
    }, [])

    return (
        <div className="app-container">
            <Header/>
            <Routes>
                <Route
                    path="/"
                    element={<Home organizations={organizations} loading={loading}/>}

                />
                <Route path="/add-organization" element={<OrganizationForm/>}/>
                <Route path="/edit-organization/:id" element={<OrganizationForm/>}/>
                <Route path="/organizations/:id" element={<OrganizationDetails/>}/>
            </Routes>
        </div>
    )
}

export default App
