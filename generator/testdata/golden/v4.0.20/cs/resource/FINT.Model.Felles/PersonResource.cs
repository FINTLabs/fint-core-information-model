using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;
using FINT.Model.Felles.Basisklasser;

namespace FINT.Model.Felles
{

    public class PersonResource : AktorResource 
    {

    
        public string Bilde { get; set; }
        public AdresseResource Bostedsadresse { get; set; }
        public DateTime? Fodselsdato { get; set; }
        public Identifikator Fodselsnummer { get; set; }
        public Personnavn Navn { get; set; }
        
            

        public void AddStatsborgerskap(Link link)
        {
            AddLink("statsborgerskap", link);
        }

        public void AddKommune(Link link)
        {
            AddLink("kommune", link);
        }

        public void AddKjonn(Link link)
        {
            AddLink("kjonn", link);
        }

        public void AddForeldreansvar(Link link)
        {
            AddLink("foreldreansvar", link);
        }

        public void AddMalform(Link link)
        {
            AddLink("malform", link);
        }

        public void AddPersonalressurs(Link link)
        {
            AddLink("personalressurs", link);
        }

        public void AddMorsmal(Link link)
        {
            AddLink("morsmal", link);
        }

        public void AddParorende(Link link)
        {
            AddLink("parorende", link);
        }

        public void AddForeldre(Link link)
        {
            AddLink("foreldre", link);
        }

        public void AddLarling(Link link)
        {
            AddLink("larling", link);
        }

        public void AddElev(Link link)
        {
            AddLink("elev", link);
        }

        public void AddOtungdom(Link link)
        {
            AddLink("otungdom", link);
        }
    }
}
