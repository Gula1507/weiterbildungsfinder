import './App.css'
import {Route, Routes} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import {Organization} from "./types/Organization.ts";
import Home from "./components/Home.tsx";
import NewOrganization from "./components/NewOrganization.tsx";
import OrganizationDetails from "./components/OrganizationDetails.tsx";


function App() {
    const [organizations, setOrganizations] = useState<Organization[]>([])

    const loadOrganizations = () => {
        axios.get("/api/organizations").then((response) => {
                setOrganizations(response.data)
            }
        )
            .catch((error) => {
                console.error(error)
            })
    }
    useEffect(() => {
        loadOrganizations()
    }, [])

    return (
        <div className="app-container">

        <Routes>
            <Route path="/" element={<Home organizations={organizations}/>}/>
            <Route path="/add-organization" element={<NewOrganization/>}/>
            <Route path="/api/organizations/:id" element={<OrganizationDetails/>}/>
        </Routes>
        </div>
    )
}

export default App
