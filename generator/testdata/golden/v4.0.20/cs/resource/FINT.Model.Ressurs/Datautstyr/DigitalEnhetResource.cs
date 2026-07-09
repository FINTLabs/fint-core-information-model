using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Ressurs.Datautstyr
{

    public class DigitalEnhetResource 
    {

    
        public Identifikator DataobjektId { get; set; }
        public bool? Flerbrukerenhet { get; set; }
        public string Navn { get; set; }
        public bool? Privateid { get; set; }
        public string Serienummer { get; set; }
        public Identifikator SystemId { get; set; }
        
        public DigitalEnhetResource()
        {
            Links = new Dictionary<string, List<Link>>();
        }

        [JsonProperty(PropertyName = "_links")]
        public Dictionary<string, List<Link>> Links { get; private set; }

        protected void AddLink(string key, Link link)
        {
            if (!Links.ContainsKey(key))
            {
                Links.Add(key, new List<Link>());
            }
            Links[key].Add(link);
        }
     
            

        public void AddAdministrator(Link link)
        {
            AddLink("administrator", link);
        }

        public void AddEier(Link link)
        {
            AddLink("eier", link);
        }

        public void AddPersonalressurs(Link link)
        {
            AddLink("personalressurs", link);
        }

        public void AddElev(Link link)
        {
            AddLink("elev", link);
        }

        public void AddStatus(Link link)
        {
            AddLink("status", link);
        }

        public void AddEnhetstype(Link link)
        {
            AddLink("enhetstype", link);
        }

        public void AddPlattform(Link link)
        {
            AddLink("plattform", link);
        }

        public void AddProdusent(Link link)
        {
            AddLink("produsent", link);
        }

        public void AddEnhetsgruppemedlemskap(Link link)
        {
            AddLink("enhetsgruppemedlemskap", link);
        }
    }
}
