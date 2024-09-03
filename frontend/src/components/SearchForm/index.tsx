import React, {useContext} from "react";
import { Form, Button, DatePicker,InputNumber,Select,Checkbox, AutoCompleteProps, AutoComplete, Typography, Row, Col } from "antd";
import dayjs from "dayjs";
import { RangePickerProps } from "antd/es/date-picker";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import FlightResultsContext from "../../contexts/FlightResultsContext";
import "./index.css";

const {RangePicker} = DatePicker;
const { Title, Paragraph} = Typography;

const SearchForm:React.FC = () =>{
    const {setFlightResults} = useContext(FlightResultsContext);
    const [options, setOptions] = React.useState<AutoCompleteProps['options']>([]);
    const navigate = useNavigate();

    const handleSearch = async (keyword: string) =>{
        if(!keyword){
            setOptions([]);
        }else{
            try{
                const response = await axios.get('http://localhost:8080/airportCodeSearch', {
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

    const disabledDate: RangePickerProps['disabledDate'] = (current) => {
        return current && current < dayjs().endOf('day');
    };

    const handleSubmit = async (values:any) =>{
        try{
            console.log(values);
            const response = await axios.get('http://localhost:8080/flightOffer',{
                params:{
                    originLocationCode: values.originLocationCode,
                    destinationLocationCode: values.destinationLocationCode,
                    departureDate: values.dates?.[0].format('YYYY-MM-DD'),
                    returnDate: values.dates?.[1]?.format('YYYY-MM-DD'),
                    adults: values.adults,
                    currencyCode: values.currencyCode,
                    nonStop: values.nonStop
                }
            });
            const data = response.data
            console.log(data);
            setFlightResults(data);
            navigate('/results');
        }catch(error){
            console.log(error);
        }
    };

    return(
        <div className="searchContainer">
            <Row> 
                <Col span={24}>
                <Title level={3}> Flight Search</Title> 
                </Col>
                
            </Row>
            <Row>
                <Col span={24}>
                    <Form onFinish={handleSubmit} layout="horizontal"
                            
                            >
                                <Form.Item 
                                    name={'originLocationCode'} 
                                    label={'Departure Airport'}
                                    rules={[{required: true, message: 'Please select a departure airport'}]}
                                >
                                            <AutoComplete
                                                options = {options}
                                                onSearch={handleSearch}
                                                placeholder = "Search your airport"
                                            >
                
                                            </AutoComplete>
                                </Form.Item>
                                <Form.Item 
                                    name={'destinationLocationCode'} 
                                    label={'Arrival Airport'}
                                    rules={[{required: true, message: 'Please select an arrival airport'}]}
                                >
                                            <AutoComplete
                                                options = {options}
                                                onSearch={handleSearch}
                                                placeholder = "Search your airport"
                                            >
                
                                            </AutoComplete>
                                </Form.Item>
                
                                
                                <Form.Item
                                    name={'dates'}
                                    label={'Dates'}
                                    rules={[{required: true, message: 'Please select at least the departure date'}]}
                                >
                                    <RangePicker
                                        allowEmpty={[false, true]}
                                        disabledDate={disabledDate}
                                        placeholder={['Departure date', 'Return date']}
                                    >
                
                                    </RangePicker>
                                </Form.Item>
                
                                <Form.Item
                                    name={'adults'}
                                    label={'Number of Adults'}
                                    rules={[{required: true, message: 'Please select the number of adults'}]}
                                    initialValue={1}
                                >
                                    <InputNumber
                                        min={1}

                                    >
                                    </InputNumber>
                                </Form.Item>
                
                                <Form.Item
                                    name={'currencyCode'}
                                    label={'Currency'}
                                    rules={[{required: true, message: 'Please select the currency'}]}
                                    initialValue={'USD'}
                                >
                                    <Select 
                                        options={[{value:'USD', label:'USD'}, {value:'MXN',label:'MXN'}, {value:'EUR',label:'EUR'}]}
                                    >
                                    </Select>
                                </Form.Item>
                
                                <Form.Item
                                    name={'nonStop'}
                                    label={'Non-stop'}
                                    valuePropName="checked"
                                    initialValue={false}
                                    //rules={[{required: true, message: 'Please select if you want stops'}]}
                                >
                                    <Checkbox>
                
                                    </Checkbox>
                                </Form.Item>
                
                                <Form.Item>
                                    <Button
                                        type="primary"
                                        htmlType="submit"
                                    >
                                        Search
                                    </Button>
                                </Form.Item>
                    </Form>
                
                </Col>
            </Row>

        </div>

    )
};

export default SearchForm;