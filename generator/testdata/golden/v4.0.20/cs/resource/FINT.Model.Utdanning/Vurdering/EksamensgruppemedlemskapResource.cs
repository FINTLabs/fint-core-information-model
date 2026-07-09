using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Utdanning.Basisklasser;

namespace FINT.Model.Utdanning.Vurdering
{

    public class EksamensgruppemedlemskapResource : Gruppemedlemskap 
    {

    
        public bool? Delegert { get; set; }
        public string Kandidatnummer { get; set; }
        
        public EksamensgruppemedlemskapResource()
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
     
            

        public void AddDelegertTil(Link link)
        {
            AddLink("delegertTil", link);
        }

        public void AddElevforhold(Link link)
        {
            AddLink("elevforhold", link);
        }

        public void AddForetrukketSkole(Link link)
        {
            AddLink("foretrukketSkole", link);
        }

        public void AddEksamensgruppe(Link link)
        {
            AddLink("eksamensgruppe", link);
        }

        public void AddNus(Link link)
        {
            AddLink("nus", link);
        }

        public void AddBetalingsstatus(Link link)
        {
            AddLink("betalingsstatus", link);
        }

        public void AddForetrukketSensor(Link link)
        {
            AddLink("foretrukketSensor", link);
        }
    }
}
