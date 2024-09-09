import React, { useContext } from "react";
import { List, Card, Row, Col, Typography, Divider, Flex, Button, Radio, RadioChangeEvent } from "antd";
import FlightResultsContext from "../../contexts/FlightResultsContext";
import { useNavigate } from "react-router-dom";
import "./index.css";
import dayjs from "dayjs";
import duration from "dayjs/plugin/duration";
import { ArrowLeftOutlined } from "@ant-design/icons";
import axios from "axios";

dayjs.extend(duration);

const { Title, Paragraph, Text} = Typography;

const Results: React.FC = () => {
    const { flightResults,setFlightResults } = useContext(FlightResultsContext);
    const {setSelectedFlight} = useContext(FlightResultsContext);
    const navigate = useNavigate();

    const handleClick = (flightOffer : any) =>{
        setSelectedFlight(flightOffer);
        console.log(flightOffer);
        navigate('/details');
    };
    
    const onChange = async (e: RadioChangeEvent) => {
        try{
            const response = await axios.get('http://localhost:8080/flightOffer/sort', {
                params:{
                    type : e.target.value
                }
            }
            );
            const data = response.data;
            setFlightResults(data);
        }catch(error){
            console.log(error);
        }
      };


    return (
       
            <Flex vertical className="resultsFlex"  style={{ height: '100%', width: '70%' }}>
                <Row align={"middle"}>
                    <Col span={24}>
                        <Title level={3}>Results</Title>
                    </Col>
                    
                </Row>
                <Row>
                    <Col span={7}>
                        <Button onClick={()=>navigate('/')} icon={<ArrowLeftOutlined />}>
                            
                        </Button>
                    </Col>
                    <Col offset={8}>
                            <Title level={5}>Sort by:</Title>
                            <Radio.Group onChange={onChange} defaultValue={'price'}>
                                <Radio.Button value={"price"}>Price</Radio.Button>
                                <Radio.Button value={"duration"}>Duration</Radio.Button>
                                <Radio.Button value={"price-duration"}>Price-Duration</Radio.Button>
                                <Radio.Button value={"duration-price"}>Duration-price</Radio.Button>
                            </Radio.Group>
                    </Col>
                </Row>
                <div style={{height:' 70vh', overflowY: 'scroll', display: 'flex', justifyContent: 'center'}}>
                    <List
                        style={{width: '90%', maxHeight:'70%'}}
                        dataSource={flightResults}
                        renderItem={(offer: any) => (
                            <List.Item onClick={()=>handleClick(offer)} key={offer.id} className="clickable">
                                {offer.itineraries.map((itinerary: any, itineraryIndex : any) => (
                                    <React.Fragment key={itineraryIndex}>
                                        <Flex>
                                            <Col>
                                                <Title level={3}> {itineraryIndex === 0 ? "Departure" : "Return"}</Title>
                                                {itinerary.segments.map((segment: any, segmentIndex : any) => (
                                                    <Row style={{ marginBottom: '16px' }} key={segmentIndex} justify={'center'}>
                                                        <Card style={{width:'100%'}}>
                                                            <Row >
                                                                <Col span={12}><Text strong> Dept. date</Text></Col>
                                                                <Col span={12}><Text strong >Arr. date</Text></Col>
                                                            </Row>
                                                            <Row className="marginBottom">
                                                                <Col span={12}><Text>{dayjs(segment.departure.at).format('YYYY-MM-DD HH:mm')} </Text></Col>
                                                                <Col span={12}><Text>{dayjs(segment.arrival.at).format('YYYY-MM-DD HH:mm')}</Text></Col>
                                                            </Row>
                                                            <Row>
                                                                <Col span={12}><Text strong>Dept. airport</Text></Col>
                                                                <Col span={12}><Text strong>Arr. airport</Text></Col>
                                                            </Row>
                                                            <Row className="marginBottom">
                                                                <Col span={12}><Text>{segment.departure.airportCommonName}({segment.departure.iataCode}) </Text></Col>
                                                                <Col span={12}><Text>{segment.arrival.airportCommonName}({segment.arrival.iataCode})</Text></Col>
                                                            </Row>
                                                            <Row>
                                                                <Col><Text strong>Duration:</Text> <Text>  {dayjs.duration(segment.duration).format('DD[d] HH[h] mm[m]')}</Text></Col>
                                                            </Row>
                                                        </Card>

                                                    </Row>
                                                ))}
                                            </Col>
                                            <Divider type="vertical" style={{ height: 'auto' }} />
                                        </Flex>
                                    </React.Fragment>
                                ))}
                                <Col>
                                                <Row>
                                                    <Title level={5}>Total time: {dayjs.duration(offer.totalDuration).format('DD[d] HH[h] mm[m]')}</Title>
                                                </Row>
                                                <Row>
                                                    <Title level={4}>$ {offer.price.total} {offer.price.currency} Total</Title>
                                                </Row>
                                                <Row>
                                                    <ul>
                                                        {offer.travelerPricings.map((price: any) => (
                                                            <li key={price.travelerId}>
                                                                {price.travelerType} {price.travelerId}: $ {price.price.total} {price.price.currency}
                                                            </li>
                                                        ))}
                                                    </ul>
                                                </Row>
                                            </Col>
                            </List.Item>
                        )}
                    />
                </div>

            </Flex>

    );
};

export default Results;
