import Commission from "../Commission/commission";



export interface InscriptionToCommission{
    id: number,
    commission:Commission,
    inscriptionDate: string,
    finishInscriptionDate:string
}

export interface PostInscriptionToCommission{
    commissionId:number,
    inscriptionDate: string,
    finishInscriptionDate:string
}