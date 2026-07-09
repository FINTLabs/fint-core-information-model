using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

namespace FINT.Model.Administrasjon.Kompleksedatatyper
{

    public class KontostrengResource 
    {

    
        public KontostrengResource()
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
     
            

        public void AddAktivitet(Link link)
        {
            AddLink("aktivitet", link);
        }

        public void AddAnlegg(Link link)
        {
            AddLink("anlegg", link);
        }

        public void AddAnsvar(Link link)
        {
            AddLink("ansvar", link);
        }

        public void AddArt(Link link)
        {
            AddLink("art", link);
        }

        public void AddDiverse(Link link)
        {
            AddLink("diverse", link);
        }

        public void AddFormal(Link link)
        {
            AddLink("formal", link);
        }

        public void AddFunksjon(Link link)
        {
            AddLink("funksjon", link);
        }

        public void AddKontrakt(Link link)
        {
            AddLink("kontrakt", link);
        }

        public void AddLopenummer(Link link)
        {
            AddLink("lopenummer", link);
        }

        public void AddObjekt(Link link)
        {
            AddLink("objekt", link);
        }

        public void AddProsjekt(Link link)
        {
            AddLink("prosjekt", link);
        }

        public void AddProsjektart(Link link)
        {
            AddLink("prosjektart", link);
        }

        public void AddRamme(Link link)
        {
            AddLink("ramme", link);
        }
    }
}
