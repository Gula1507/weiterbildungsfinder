import './App.css'
import {Route, Routes} from "react-router-dom";
import Home from "./components/Home.tsx"
import {useEffect, useState} from "react";
import axios from "axios";
import {Organization} from "./types/Organization.ts";


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
        <Routes>
            <Route path="/" element={<Home organizations={organizations}/>}></Route>
        </Routes>
    )
}

export default App
