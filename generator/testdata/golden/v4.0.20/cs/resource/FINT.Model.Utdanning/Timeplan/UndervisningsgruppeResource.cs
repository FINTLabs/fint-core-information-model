using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Utdanning.Basisklasser;

namespace FINT.Model.Utdanning.Timeplan
{

    public class UndervisningsgruppeResource : Gruppe 
    {

    
        public UndervisningsgruppeResource()
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
     
            

        public void AddUndervisningsforhold(Link link)
        {
            AddLink("undervisningsforhold", link);
        }

        public void AddFag(Link link)
        {
            AddLink("fag", link);
        }

        public void AddTermin(Link link)
        {
            AddLink("termin", link);
        }

        public void AddSkole(Link link)
        {
            AddLink("skole", link);
        }

        public void AddSkolear(Link link)
        {
            AddLink("skolear", link);
        }

        public void AddTime(Link link)
        {
            AddLink("time", link);
        }

        public void AddGruppemedlemskap(Link link)
        {
            AddLink("gruppemedlemskap", link);
        }
    }
}
