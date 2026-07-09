using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;
using FINT.Model.Arkiv.Noark;

namespace FINT.Model.Arkiv.Personal
{

    public class PersonalmappeResource : SaksmappeResource 
    {

    
        public Personnavn Navn { get; set; }
        
            

        public void AddPerson(Link link)
        {
            AddLink("person", link);
        }

        public void AddLeder(Link link)
        {
            AddLink("leder", link);
        }

        public void AddArbeidssted(Link link)
        {
            AddLink("arbeidssted", link);
        }

        public void AddPersonalressurs(Link link)
        {
            AddLink("personalressurs", link);
        }
    }
}
