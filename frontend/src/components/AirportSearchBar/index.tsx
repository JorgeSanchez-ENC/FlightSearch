import React from 'react';
import { AutoComplete } from 'antd';
import type { AutoCompleteProps } from 'antd';
import axios, { AxiosRequestConfig } from "axios";

const API_URL = 'http://localhost:8080/airportCodeSearch'

const AirportSearchBar = () => {
    

    const [options, setOptions] = React.useState<AutoCompleteProps['options']>([]);

    const handleSearch = async (keyword: string) =>{
        if(!keyword){
            setOptions([]);
        }else{
            try{
                const response = await axios.get(API_URL, {
                    params:{
                        keyword : keyword
                    }
                }
                );
                const data = response.data;
                setOptions(data.map((airport: { iataCode: any; name: any; })=>({
                    value: airport.iataCode,
                    label: `${airport.name} ${airport.iataCode}`,
                })));
            }catch(error){
                console.log(error);
            }
        }
    };

    return(
        <AutoComplete
            options = {options}
            onSearch={handleSearch}
            placeholder = "Search your airport"
        >

        </AutoComplete>
    );
};

export default AirportSearchBar;