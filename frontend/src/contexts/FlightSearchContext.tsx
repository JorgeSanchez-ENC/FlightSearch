import React from "react";
import { createContext, ReactNode, useState } from "react";

type params = {
    originLocationCode : string | null;
    destinationLocationCode : string | null;
    departureDate : string | null;
    returnDate : string | null;
    adults : number | null;
    currencyCode : string | null;
}

interface FlightSearchContextType {
    params : params;
    setParams: React.Dispatch<React.SetStateAction<params>>;
}

const FlightSearchContext = createContext<any>(null);


export const FlightSearchContextProvider = ({children} :{children : ReactNode}) =>{

    const[params,setParams] = useState<params>({
        originLocationCode : null,
        destinationLocationCode : null,
        departureDate : null,
        returnDate :  null,
        adults :  null,
        currencyCode : null,
    })


    return (
        <FlightSearchContext.Provider value={{params, setParams}}>
            {children}
        </FlightSearchContext.Provider>
    )

}